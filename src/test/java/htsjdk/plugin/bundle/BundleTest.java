package htsjdk.plugin.bundle;

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

// Example JSON :
//{
// "schemaName":"htsbundle",
// "schemaVersion":"0.1.0",
// "INDEX":{"path":"myFile.bai","subtype":"NONE"},
// "READS":{"path":"myFile.bam","subtype":"NONE"}
// }

//TODO: test serialized tag, attributes
//TODO: test retrieving OutputBundles from JSON
//TODO: are JSON names case sensitive ?

public class BundleTest extends HtsjdkTest {

    final static IOPath READS_FILE = new HtsPath("myreads.bam");
    final static IOPath READS_INDEX = new HtsPath("myreads.bai");

    // input resources
    final InputResource inputReadsWithSubType = new InputIOPathResource(
            READS_FILE,
            BundleResourceType.READS,
            BundleResourceType.READS_BAM);
    final InputResource inputReadsNoSubType = new InputIOPathResource(
            READS_FILE,
            BundleResourceType.READS);
    final InputResource inputIndexWithSubType = new InputIOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX,
            BundleResourceType.READS_INDEX_BAI);
    final InputResource inputIndexNoSubType = new InputIOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX);

    // output resources
    final OutputResource outputReadsWithSubType = new OutputIOPathResource(
            READS_FILE,
            BundleResourceType.READS,
            BundleResourceType.READS_BAM);
    final OutputResource outputReadsNoSubType = new OutputIOPathResource(
            READS_FILE,
            BundleResourceType.READS);
    final OutputResource outputIndexWithSubType = new OutputIOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX,
            BundleResourceType.READS_INDEX_BAI);
    final OutputResource outputIndexNoSubType = new OutputIOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX);

    @DataProvider(name="inputResourceEquality")
    public Object[][] getInputResourceEquality() {
        return new Object[][]{
                { inputReadsWithSubType, inputReadsWithSubType, true },
                { inputReadsNoSubType, inputReadsNoSubType, true },

                {inputReadsWithSubType, inputReadsNoSubType, false },
        };
    }

    @Test(dataProvider="inputResourceEquality")
    public void testInputResourceEquality(
            final InputResource inputResource1,
            final InputResource inputResource2,
            final boolean expectedEquals) {
        Assert.assertEquals(inputResource1.equals(inputResource2), expectedEquals);
        Assert.assertEquals(inputResource2.equals(inputResource1), expectedEquals);
    }

    @DataProvider(name="outputResourceEquality")
    public Object[][] getOutputResourceEquality() {
        return new Object[][]{
                { outputReadsWithSubType, outputReadsNoSubType, false },
        };
    }

    @Test(dataProvider = "outputResourceEquality")
    public void testOutputResourceEquality(
            final OutputResource outputResource1,
            final OutputResource outputResource2,
            final boolean expectedEquals) {
        Assert.assertEquals(outputResource1.equals(outputResource2), expectedEquals);
        Assert.assertEquals(outputResource2.equals(outputResource1), expectedEquals);
    }

    @Test
    public void testInputOutputResourceInequality() {
        final InputResource inputResource = new InputIOPathResource(
                new HtsPath("myreads.bam"),
                BundleResourceType.READS,
                BundleResourceType.READS_BAM);
        final OutputResource outputResource = new OutputIOPathResource(
                new HtsPath("myreads.bam"),
                BundleResourceType.READS,
                BundleResourceType.READS_BAM);
        Assert.assertNotEquals(inputResource, outputResource);
    }

    @Test
    public void testInputBundleIterator() {
        final InputBundle inputBundle =
                InputBundleBuilder.start()
                        .add(inputReadsWithSubType)
                        .add(inputIndexNoSubType)
                        .getBundle();
        final Iterator<InputResource> it = inputBundle.iterator();
        while (it.hasNext()) {
            final InputResource ir = it.next();
            if (ir.getContentType().equals(BundleResourceType.READS)) {
                Assert.assertEquals(ir, inputReadsWithSubType);
            } else {
                Assert.assertEquals(ir, inputIndexNoSubType);
            }
        }
    }

    @Test
    public void testOutputBundleIterator() {
        final OutputBundle outputBundle =
                OutputBundleBuilder.start()
                        .add(outputReadsWithSubType)
                        .add(outputIndexNoSubType)
                        .getBundle();
        final Iterator<OutputResource> it = outputBundle.iterator();
        while (it.hasNext()) {
            final OutputResource ir = it.next();
            if (ir.getContentType().equals(BundleResourceType.READS)) {
                Assert.assertEquals(ir, outputReadsWithSubType);
            } else {
                Assert.assertEquals(ir, outputIndexNoSubType);
            }
        }
    }

    @DataProvider(name = "validBundleJSON")
    public Object[][] getValidBundleJSON() {
        return new Object[][]{
                //NOTE that these JSON strings contain the resources in the same order that they're serialized by mjson
                // so that we can uses these cases to validate in both directions

                // json string, corresponding array of resources
                { "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\",\"subtype\":\"BAM\"}}",
                        Arrays.asList(inputReadsWithSubType) },
                { "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\"}}",
                        Arrays.asList(inputReadsNoSubType) },
                { "{\"schemaVersion\":\"0.1.0\",\"INDEX\":{\"path\":\"myreads.bai\",\"subtype\":\"BAI\"},\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\",\"subtype\":\"BAM\"}}",
                        Arrays.asList(inputReadsWithSubType, inputIndexWithSubType) },
                { "{\"schemaVersion\":\"0.1.0\",\"INDEX\":{\"path\":\"myreads.bai\",\"subtype\":\"BAI\"}," +
                        "\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\"}}",
                        Arrays.asList(inputReadsNoSubType, inputIndexWithSubType) },
                { "{\"schemaVersion\":\"0.1.0\",\"INDEX\":{\"path\":\"myreads.bai\"}," +
                        "\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\",\"subtype\":\"BAM\"}}",
                        Arrays.asList(inputReadsWithSubType, inputIndexNoSubType) },
                { "{\"schemaVersion\":\"0.1.0\",\"INDEX\":{\"path\":\"myreads.bai\"}," +
                        "\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads.bam\"}}",
                        Arrays.asList(inputReadsNoSubType, inputIndexNoSubType) },

                // bundle with a single resources thats a custom content type
                { "{\"schemaVersion\":\"0.1.0\",\"CUSTOM\":{\"path\":\"myreads.CUSTOM\"},\"schemaName\":\"htsbundle\"}",
                        Arrays.asList(new InputIOPathResource(new HtsPath("myreads.CUSTOM"),"CUSTOM")) },

                // three resources, one of which is a custom content type
                { "{\"schemaVersion\":\"0.1.0\",\"CUSTOM\":{\"path\":\"myreads.CUSTOM\"}," +
                        "\"INDEX\":{\"path\":\"myreads.bai\"},\"schemaName\":\"htsbundle\"," +
                        "\"READS\":{\"path\":\"myreads.bam\"}}",
                        Arrays.asList(
                                inputReadsNoSubType,
                                inputIndexNoSubType,
                                new InputIOPathResource(new HtsPath("myreads.CUSTOM"),"CUSTOM"))},
        };
    }

    @Test(dataProvider = "validBundleJSON")
    public void testToJSONValid(final String jsonString, final List<InputResource> resources) {
        final InputBundleBuilder inputBundleBuilder = InputBundleBuilder.start();
        resources.forEach(resource -> {
            inputBundleBuilder.add(resource);
        });
        final InputBundle inputBundle = inputBundleBuilder.getBundle();
        final String actualJSONString = inputBundle.toJSON();
        Assert.assertEquals(actualJSONString, jsonString);
    }

    @Test(dataProvider = "validBundleJSON")
    public void testFromJSONValid(final String jsonString, final List<InputResource> resources) {
        final InputBundle bundleFromJSON = new InputBundle(jsonString);
        Assert.assertNotNull(bundleFromJSON);
        resources.forEach(expectedResource -> {
            final Optional<InputResource> jsonResource = bundleFromJSON.get(expectedResource.getContentType());
            Assert.assertTrue(jsonResource.isPresent());
            Assert.assertEquals(jsonResource.get(), expectedResource);
        });
    }

    final static class TestHtsPathSubclass extends HtsPath {
        public TestHtsPathSubclass(final String pathString) {
            super(pathString);
        }
    }

    @Test(dataProvider = "validBundleJSON")
    public void testFromJSONValidWithPathOverride(final String jsonString, final List<InputResource> expectedResources) {
        final InputBundle bundleFromJSON = new InputBundle(jsonString, TestHtsPathSubclass::new);
        Assert.assertNotNull(bundleFromJSON);
        expectedResources.forEach(expectedResource -> {
            final Optional<InputResource> jsonResource = bundleFromJSON.get(expectedResource.getContentType());
            Assert.assertTrue(jsonResource.isPresent());
            //NOTE: we don't test the individual resources for equality here, since the expected resources were
            // not created with a custom path override, so a resource equality test would fail because HtsPath
            // equality test would fail. Instead we just verify that the classes resulting from JSON serialization
            // use our custom HtsPath-derived class.
            final InputIOPathResource ioPathResource = ((InputIOPathResource) jsonResource.get());
            Assert.assertTrue(ioPathResource.getIOPath().isPresent());
            final IOPath ioPath = ioPathResource.getIOPath().get();
            Assert.assertEquals(ioPath.getClass().getSimpleName(), TestHtsPathSubclass.class.getSimpleName());
            final TestHtsPathSubclass subClass = (TestHtsPathSubclass) ioPath;
        });
    }

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

    @DataProvider(name = "invalidBundleJSON")
    public Object[][] getInvalidBundleJSON() {
        return new Object[][]{
                { null, "cannot be null" },
                { "", "end of input" },
                // missing schema name
                { "{}" , "missing the required property schemaName" },
                // still missing schema name
                { "{\"schemaVersion\":\"0.1.0\"}", "missing the required property schemaName" },
                // incorrect schema name
                { "{\"schemaName\":\"bogusname\", \"schemaVersion\":\"0.1.0\"}", "Expected bundle schema" },
                // missing schema version
                { "{\"schemaName\":\"htsbundle\"}", "missing required property schemaVersion" },
                // incorrect schema version
                { "{\"schemaName\":\"htsbundle\", \"schemaVersion\":\"99.99.99\"}", "Expected bundle version" },
                // no enclosing {} -> UnsupportedOperationException (no text message)
                {"\"schemaName\":\"htsbundle\", \"schemaVersion\":\"0.1.0\"", "", },
        };
    }

    @Test(dataProvider = "invalidBundleJSON", expectedExceptions = IllegalArgumentException.class)
    public void testFromJSONInvalid(final String jsonString, final String expectedMessageFragment) {
        try {
            new InputBundle(jsonString);
        } catch (final Exception e) {
            Assert.assertTrue(e.getMessage().contains(expectedMessageFragment));
            throw e;
        }
    }

}