package htsjdk.beta.plugin.bundle;

import htsjdk.HtsjdkTest;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class InputBundleTest extends HtsjdkTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testToJSONNonIOPath() throws IOException {
        try (final InputStream is = new ByteArrayInputStream(new byte[0])) {
            final InputBundle inputBundle = InputBundleBuilder.start()
                    .add(new InputStreamResource(is,"displayName","contentType"))
                    .getBundle();
            // can't serialize a resource that isn't backed by an IOPath
            inputBundle.toJSON();
        } catch (final IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Bundle resource requires a valid path to be serialized"));
            throw e;
        }
    }

    @Test
    public void testInputBundleIterator() {
        final InputBundle inputBundle =
                InputBundleBuilder.start()
                        .add(BundleResourceTestData.inputReadsWithSubType.get())
                        .add(BundleResourceTestData.inputIndexNoSubType.get())
                        .getBundle();
        final Iterator<InputResource> it = inputBundle.iterator();
        while (it.hasNext()) {
            final InputResource ir = it.next();
            if (ir.getContentType().equals(BundleResourceType.READS)) {
                Assert.assertEquals(ir, BundleResourceTestData.inputReadsWithSubType.get());
            } else {
                Assert.assertEquals(ir, BundleResourceTestData.inputIndexNoSubType.get());
            }
        }
    }

    @DataProvider(name = "roundTripJSON")
    public Object[][] getRoundTripJSON() {
        return new Object[][]{
                //NOTE that these JSON strings contain the resources in the same order that they're serialized by mjson
                // so that we can uses these cases to validate in both directions

                // json string, corresponding array of resources

                // input resources
                {
                        "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\",\"subtype\":\"BAM\"}}",
                        Arrays.asList(BundleResourceTestData.inputReadsWithSubType.get())
                },
                {
                        "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\"}}",
                        Arrays.asList(BundleResourceTestData.inputReadsNoSubType.get())
                },
                {
                        "{\"schemaVersion\":\"0.1.0\",\"INDEX\":{\"path\":\"myreads.bai\",\"subtype\":\"BAI\"},\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\",\"subtype\":\"BAM\"}}",
                        Arrays.asList(BundleResourceTestData.inputReadsWithSubType.get(), BundleResourceTestData.inputIndexWithSubType.get())
                },
                {
                        "{\"schemaVersion\":\"0.1.0\",\"INDEX\":{\"path\":\"myreads.bai\",\"subtype\":\"BAI\"}," +
                                "\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\"}}",
                        Arrays.asList(BundleResourceTestData.inputReadsNoSubType.get(), BundleResourceTestData.inputIndexWithSubType.get())
                },
                {
                        "{\"schemaVersion\":\"0.1.0\",\"INDEX\":{\"path\":\"myreads.bai\"}," +
                                "\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\",\"subtype\":\"BAM\"}}",
                        Arrays.asList(BundleResourceTestData.inputReadsWithSubType.get(), BundleResourceTestData.inputIndexNoSubType.get()) },
                {
                        "{\"schemaVersion\":\"0.1.0\",\"INDEX\":{\"path\":\"myreads.bai\"}," +
                                "\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\"}}",
                        Arrays.asList(BundleResourceTestData.inputReadsNoSubType.get(), BundleResourceTestData.inputIndexNoSubType.get())
                },

                // bundle with a single resource that has a custom content type
                {
                        "{\"schemaVersion\":\"0.1.0\",\"CUSTOM\":{\"path\":\"myreads.CUSTOM\"},\"schemaName\":\"htsbundle\"}",
                        Arrays.asList(new InputIOPathResource(new HtsPath("myreads.CUSTOM"),"CUSTOM"))
                },

                // three resources, one of which is a custom content type
                {
                        "{\"schemaVersion\":\"0.1.0\",\"CUSTOM\":{\"path\":\"myreads.CUSTOM\"}," +
                                "\"INDEX\":{\"path\":\"myreads.bai\"},\"schemaName\":\"htsbundle\"," +
                                "\"READS\":{\"path\":\"myreads.bam\"}}",
                        Arrays.asList(
                                BundleResourceTestData.inputReadsNoSubType.get(),
                                BundleResourceTestData.inputIndexNoSubType.get(),
                                new InputIOPathResource(new HtsPath("myreads.CUSTOM"),"CUSTOM"))
                },
        };
    }

    @Test(dataProvider = "roundTripJSON")
    public void testRoundTripJSON(final String jsonString, final List<InputResource> resources) {
        final InputBundle bundleFromResources = new InputBundle(resources);
        final String actualJSONString = bundleFromResources.toJSON();
        Assert.assertEquals(actualJSONString, jsonString);

        System.out.println(jsonString);
        // now recreate the bundle from JSON
        final InputBundle bundleFromJSON = new InputBundle(jsonString);
        Assert.assertNotNull(bundleFromJSON);
        resources.forEach(expectedResource -> {
            final Optional<InputResource> jsonResource = bundleFromJSON.get(expectedResource.getContentType());
            Assert.assertTrue(jsonResource.isPresent());
            Assert.assertEquals(jsonResource.get(), expectedResource);
        });
    }

    @Test(dataProvider = "roundTripJSON")
    public void testFromJSONValidWithPathOverride(final String jsonString, final List<InputResource> expectedResources) {
        final InputBundle bundleFromJSON = new InputBundle(jsonString, BundleResourceTestData.TestHtsPathSubclass::new);
        Assert.assertNotNull(bundleFromJSON);
        expectedResources.forEach(expectedResource -> {
            final Optional<InputResource> jsonResource = bundleFromJSON.get(expectedResource.getContentType());
            Assert.assertTrue(jsonResource.isPresent());
            //NOTE: we don't test the individual resources for equality here, since the expected resources
            // don't have a custom path type, so a resource equality test would fail because the HtsPath
            // equality test would fail. Instead we just verify that the classes resulting from JSON serialization
            // use our custom HtsPath-derived class.
            final InputIOPathResource ioPathResource = ((InputIOPathResource) jsonResource.get());
            Assert.assertTrue(ioPathResource.getIOPath().isPresent());
            final IOPath ioPath = ioPathResource.getIOPath().get();
            Assert.assertEquals(ioPath.getClass().getSimpleName(), BundleResourceTestData.TestHtsPathSubclass.class.getSimpleName());
            final BundleResourceTestData.TestHtsPathSubclass subClass = (BundleResourceTestData.TestHtsPathSubclass) ioPath;
        });
    }

}
