package htsjdk.plugin.bundle;

import htsjdk.HtsjdkTest;
import htsjdk.io.IOPath;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class OutputBundleTest extends HtsjdkTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testToJSONNonIOPath() throws IOException {
        try (final OutputStream is = new ByteArrayOutputStream()) {
            final OutputBundle inputBundle = OutputBundleBuilder.start()
                    .add(new OutputStreamResource(is,"displayName","contentType"))
                    .getBundle();
            // can't serialize a resource that isn't backed by an IOPath
            inputBundle.toJSON();
        } catch (final IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Bundle resource requires a valid path to be serialized"));
            throw e;
        }
    }

    @Test
    public void testOutputBundleIterator() {
        final OutputBundle outputBundle =
                OutputBundleBuilder.start()
                        .add(BundleResourceTestData.outputReadsWithSubType.get())
                        .add(BundleResourceTestData.outputIndexNoSubType.get())
                        .getBundle();
        final Iterator<OutputResource> it = outputBundle.iterator();
        while (it.hasNext()) {
            final OutputResource ir = it.next();
            if (ir.getContentType().equals(BundleResourceType.READS)) {
                Assert.assertEquals(ir, BundleResourceTestData.outputReadsWithSubType.get());
            } else {
                Assert.assertEquals(ir, BundleResourceTestData.outputIndexNoSubType.get());
            }
        }
    }

    @DataProvider(name = "roundTripJSON")
    public Object[][] getRoundTripJSON() {
        return new Object[][]{
                //NOTE that these JSON strings contain the resources in the same order that they're serialized by mjson
                // so that we can uses these cases to validate in both directions

                // json string, corresponding array of resources

                // output resources
                {
                        "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\",\"subtype\":\"BAM\"}}",
                        Arrays.asList(BundleResourceTestData.outputReadsWithSubType.get())
                },
                {
                        "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\"}}",
                        Arrays.asList(BundleResourceTestData.outputReadsNoSubType.get())
                },
                // single tag
                {
                        "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"my.bam\",\"subtype\":\"BAM\",\"tag\":\"testTAG\"}}",
                        Arrays.asList(BundleResourceTestData.outputReadsWithTag.get())
                },
                // tag, plus one attribute
                {
                        "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"my.bam\"," +
                                "\"subtype\":\"BAM\",\"attributes\":{\"attribute1\":\"value1\"},\"tag\":\"testTAG\"}}",
                        Arrays.asList(BundleResourceTestData.outputReadsWithTagOneAttribute.get())
                },
                // tag plus two attributes
                {
                        "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"my.bam\"," +
                                "\"subtype\":\"BAM\",\"attributes\":{\"attribute1\":\"value1\",\"attribute2\":\"value2\"},\"tag\":\"testTAG\"}}",
                        Arrays.asList(BundleResourceTestData.outputReadsWithTagTwoAttributes.get())
                },
                // attributes only
                {
                        "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"my.bam\"," +
                                "\"subtype\":\"BAM\",\"attributes\":{\"attribute1\":\"value1\",\"attribute2\":\"value2\"}}}",
                        Arrays.asList(BundleResourceTestData.outputReadsNoTagTwoAttributes.get())
                },
        };
    }

    @Test(dataProvider = "roundTripJSON")
    public void testToJSONValid(final String jsonString, final List<OutputResource> resources) {
        final OutputBundle inputBundle = new OutputBundle(resources);
        final String actualJSONString = inputBundle.toJSON();
        Assert.assertEquals(actualJSONString, jsonString);

        // now recreate the bundle from json
        final OutputBundle bundleFromJSON = new OutputBundle(jsonString);
        Assert.assertNotNull(bundleFromJSON);
        resources.forEach(expectedResource -> {
            final Optional<OutputResource> jsonResource = bundleFromJSON.get(expectedResource.getContentType());
            Assert.assertTrue(jsonResource.isPresent());
            Assert.assertEquals(jsonResource.get(), expectedResource);
        });
    }

    @Test(dataProvider = "roundTripJSON")
    public void testFromJSONValid(final String jsonString, final List<OutputResource> resources) {
    }

    @Test(dataProvider = "roundTripJSON")
    public void testFromJSONValidWithPathOverride(final String jsonString, final List<OutputResource> expectedResources) {
        final OutputBundle bundleFromJSON = new OutputBundle(jsonString, BundleResourceTestData.TestHtsPathSubclass::new);
        Assert.assertNotNull(bundleFromJSON);
        expectedResources.forEach(expectedResource -> {
            final Optional<OutputResource> jsonResource = bundleFromJSON.get(expectedResource.getContentType());
            Assert.assertTrue(jsonResource.isPresent());
            //NOTE: we don't test the individual resources for equality here, since the expected resources
            // don't have a custom path type, so a resource equality test would fail because the HtsPath
            // equality test would fail. Instead we just verify that the classes resulting from JSON serialization
            // use our custom HtsPath-derived class.
            final OutputIOPathResource ioPathResource = ((OutputIOPathResource) jsonResource.get());
            Assert.assertTrue(ioPathResource.getIOPath().isPresent());
            final IOPath ioPath = ioPathResource.getIOPath().get();
            Assert.assertEquals(ioPath.getClass().getSimpleName(), BundleResourceTestData.TestHtsPathSubclass.class.getSimpleName());
            final BundleResourceTestData.TestHtsPathSubclass subClass = (BundleResourceTestData.TestHtsPathSubclass) ioPath;
        });
    }

}
