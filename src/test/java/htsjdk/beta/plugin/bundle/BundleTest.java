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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

// Example JSON :
//{
// "schemaName":"htsbundle",
// "schemaVersion":"0.1.0",
// "READS",
// "READS_INDEX":{"path":"myFile.bai","subtype":"NONE"},
// "READS":{"path":"myFile.bam","subtype":"NONE"}
// }

public class BundleTest extends HtsjdkTest {

    @Test
    public void testPrimaryResource() {
        final String primaryKey = BundleResourceType.READS;
        final IOPathResource ioPathResource = new IOPathResource(
                new HtsPath("somefile.bam"),
                BundleResourceType.READS);
        final Bundle bundle = new Bundle(primaryKey, Collections.singletonList(ioPathResource));
        Assert.assertEquals(bundle.getPrimaryResourceKey(), primaryKey);
        Assert.assertEquals(bundle.getPrimaryResource(), ioPathResource);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullPrimaryResource() {
        new Bundle(null, Collections.singletonList(
                new IOPathResource(new HtsPath("somefile.bam"), BundleResourceType.READS)));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testPrimaryResourceNotInBundle() {
        // the primary resource is specified but the resource specified is not in the bundle
        final String primaryKey = "MISSING_RESOURCE";
        final IOPathResource ioPathResource = new IOPathResource(
                new HtsPath("somefile.bam"),
                BundleResourceType.READS);
        try {
            new Bundle(primaryKey, Collections.singletonList(ioPathResource));
        } catch (final IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("not present in the resource list"));
            throw e;
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDuplicateResource() {
        final String primaryKey = BundleResourceType.READS;
        final IOPathResource ioPathResource = new IOPathResource(
                new HtsPath("somefile.bam"),
                BundleResourceType.READS);
        try {
            new Bundle(primaryKey, Arrays.asList(ioPathResource, ioPathResource));
        } catch (final IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Attempt to add a duplicate resource"));
            throw e;
        }
    }

    @Test
    public void testResourceIterator() {
        final Bundle bundle =
                BundleBuilder.start()
                        .addPrimary(BundleResourceTestData.readsWithSubContentType)
                        .add(BundleResourceTestData.indexNoSubContentType)
                        .getBundle();
        final Iterator<BundleResource> it = bundle.iterator();
        while (it.hasNext()) {
            final BundleResource ir = it.next();
            if (ir.getContentType().equals(BundleResourceType.READS)) {
                Assert.assertEquals(ir, BundleResourceTestData.readsWithSubContentType);
            } else {
                Assert.assertEquals(ir, BundleResourceTestData.indexNoSubContentType);
            }
        }
    }

    @DataProvider(name = "roundTripJSON")
    public Object[][] getRoundTripJSON() {
        final IOPathResource CUSTOM_RESOURCE =
                new IOPathResource(new HtsPath("file://myreads.CUSTOM"),"CUSTOM");

        return new Object[][]{
                // NOTE that these JSON strings contain the resources in the same order as they're serialized by
                // mjson so that we can validate conversions in both directions.
                //
                // The strings need to contain path strings that are full URIs, since that's what the JSON
                // serializer for bundles uses when serializing IOPaths as JSON.

                // json string, primary key, corresponding array of resources

                {
                    "{\n" +
                            "  \"schemaName\":\"htsbundle\",\n" +
                            "  \"schemaVersion\":\"0.1.0\",\n" +
                            "  \"primary\":\"READS\",\n" +
                            "  \"READS\":{\"path\":\"" +
                                    getURIStringFromIOPath(BundleResourceTestData.readsWithSubContentType) +
                                    "\",\"subtype\":\"BAM\"}\n" +
                            "}\n",
                        BundleResourceType.READS,
                        Arrays.asList(BundleResourceTestData.readsWithSubContentType)
                },
                {
                    "{\n" +
                            "  \"schemaName\":\"htsbundle\",\n" +
                            "  \"schemaVersion\":\"0.1.0\",\n" +
                            "  \"primary\":\"READS\",\n" +
                            "  \"READS\":{\"path\":\"" +
                                    getURIStringFromIOPath(BundleResourceTestData.readsNoSubContentType) + "\"}\n" +
                            "}\n",
                        BundleResourceType.READS,
                        Arrays.asList(BundleResourceTestData.readsNoSubContentType)
                },
                {
                        "{\n" +
                                "  \"schemaName\":\"htsbundle\",\n" +
                                "  \"schemaVersion\":\"0.1.0\",\n" +
                                "  \"primary\":\"READS\",\n" +
                                "  \"READS_INDEX\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.indexWithSubContentType) +
                                        "\",\"subtype\":\"BAI\"},\n" +
                                "  \"READS\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.readsWithSubContentType) +
                                        "\",\"subtype\":\"BAM\"}\n" +
                                "}\n",
                        BundleResourceType.READS,
                        Arrays.asList(
                                BundleResourceTestData.readsWithSubContentType,
                                BundleResourceTestData.indexWithSubContentType)
                },
                {
                        "{\n" +
                                "  \"schemaName\":\"htsbundle\",\n" +
                                "  \"schemaVersion\":\"0.1.0\",\n" +
                                "  \"primary\":\"READS\",\n" +
                                "  \"READS_INDEX\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.indexWithSubContentType) +
                                        "\",\"subtype\":\"BAI\"},\n" +
                                "  \"READS\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.readsNoSubContentType) +
                                        "\"}\n" +
                                "}\n",
                        BundleResourceType.READS,
                        Arrays.asList(
                                BundleResourceTestData.readsNoSubContentType,
                                BundleResourceTestData.indexWithSubContentType)
                },
                {
                        "{\n" +
                                "  \"schemaName\":\"htsbundle\",\n" +
                                "  \"schemaVersion\":\"0.1.0\",\n" +
                                "  \"primary\":\"READS\",\n" +
                                "  \"READS_INDEX\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.indexNoSubContentType) +
                                        "\"},\n" +
                                "  \"READS\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.readsWithSubContentType) +
                                        "\",\"subtype\":\"BAM\"}\n" +
                                "}\n",
                        BundleResourceType.READS,
                        Arrays.asList(
                                BundleResourceTestData.readsWithSubContentType,
                                BundleResourceTestData.indexNoSubContentType) },
                {
                        "{\n" +
                                "  \"schemaName\":\"htsbundle\",\n" +
                                "  \"schemaVersion\":\"0.1.0\",\n" +
                                "  \"primary\":\"READS\",\n" +
                                "  \"READS_INDEX\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.indexNoSubContentType) + "\"},\n" +
                                "  \"READS\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.readsNoSubContentType) + "\"}\n" +
                                "}\n",
                        BundleResourceType.READS,
                        Arrays.asList(
                                BundleResourceTestData.readsNoSubContentType,
                                BundleResourceTestData.indexNoSubContentType)
                },

                // bundle with a single resource that has a custom content type
                {
                        "{\n" +
                                "  \"schemaName\":\"htsbundle\",\n" +
                                "  \"schemaVersion\":\"0.1.0\",\n" +
                                "  \"primary\":\"CUSTOM\",\n" +
                                "  \"CUSTOM\":{\"path\":\"" + getURIStringFromIOPath(CUSTOM_RESOURCE) + "\"}\n" +
                                "}\n",
                        "CUSTOM",
                        Arrays.asList(CUSTOM_RESOURCE)
                },

                // three resources, one of which is a custom content type
                {
                        "{\n" +
                                "  \"schemaName\":\"htsbundle\",\n" +
                                "  \"schemaVersion\":\"0.1.0\",\n" +
                                "  \"primary\":\"READS\",\n" +
                                "  \"READS_INDEX\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.indexNoSubContentType) +
                                        "\"},\n" +
                                "  \"CUSTOM\":{\"path\":\"" + getURIStringFromIOPath(CUSTOM_RESOURCE) + "\"},\n" +
                                "  \"READS\":{\"path\":\"" +
                                        getURIStringFromIOPath(BundleResourceTestData.readsNoSubContentType) +
                                        "\"}\n" +
                                "}\n",
                        "READS",
                        Arrays.asList(
                                BundleResourceTestData.readsNoSubContentType,
                                BundleResourceTestData.indexNoSubContentType,
                                CUSTOM_RESOURCE)
                },
        };
    }

    @Test(dataProvider = "roundTripJSON")
    public void testRoundTripJSON(final String jsonString, final String primaryKey, final List<BundleResource> resources) {
        final Bundle bundleFromResources = new Bundle(primaryKey, resources);
        final String actualJSONString = bundleFromResources.toJSON();
        Assert.assertEquals(actualJSONString, jsonString);

        // now recreate the bundle from JSON
        final Bundle bundleFromJSON = new Bundle(jsonString);

        Assert.assertNotNull(bundleFromJSON);
        Assert.assertEquals(bundleFromJSON.getPrimaryResourceKey(), primaryKey);

        resources.forEach(expectedResource -> {
            final Optional<BundleResource> jsonResource = bundleFromJSON.get(expectedResource.getContentType());
            Assert.assertTrue(jsonResource.isPresent());
            Assert.assertEquals(jsonResource.get(), expectedResource);
        });
    }

    @Test(dataProvider = "roundTripJSON")
    public void testFromJSONValidWithPathOverride(final String jsonString, final String primaryKey, final List<BundleResource> expectedResources) {
        final Bundle bundleFromJSON = new Bundle(jsonString, BundleResourceTestData.CustomHtsPath::new);
        Assert.assertNotNull(bundleFromJSON);
        Assert.assertEquals(bundleFromJSON.getPrimaryResourceKey(), primaryKey);
        expectedResources.forEach(expectedResource -> {
            final Optional<BundleResource> jsonResource = bundleFromJSON.get(expectedResource.getContentType());
            Assert.assertTrue(jsonResource.isPresent());
            //NOTE: we don't test the individual resources for equality here, since the expected resources
            // don't have a custom path type, so a resource equality test would fail because the HtsPath
            // equality test would fail. Instead we just verify that the classes resulting from JSON serialization
            // use our custom HtsPath-derived class.
            final IOPathResource ioPathResource = ((IOPathResource) jsonResource.get());
            Assert.assertTrue(ioPathResource.getIOPath().isPresent());
            final IOPath ioPath = ioPathResource.getIOPath().get();
            Assert.assertEquals(ioPath.getClass().getSimpleName(), BundleResourceTestData.CustomHtsPath.class.getSimpleName());
            // typecast just to make sure
            final BundleResourceTestData.CustomHtsPath subClass = (BundleResourceTestData.CustomHtsPath) ioPath;
        });
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
                { "{\"schemaName\":\"bogusname\", \"schemaVersion\":\"0.1.0\"}", "Expected bundle schema name" },

                // missing schema version
                { "{\"schemaName\":\"htsbundle\"}", "missing the required property schemaVersion" },

                // incorrect schema version
                { "{\"schemaName\":\"htsbundle\", \"schemaVersion\":\"99.99.99\"}", "Expected bundle schema version" },

                // missing primary property
                { "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads" +
                                ".bam\",\"subtype\":\"BAM\"}}",
                   "missing the required property primary"},

                // primary property is present, but the resource it specifies is not in the bundle
                { "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads" +
                        ".bam\",\"subtype\":\"BAM\"},\"primary\":\"MISSING_RESOURCE\"}",
                   "resource specified by the primary property is not present in the resource list"},

                // syntax error (missing quote in before schemaName
                { "{\"schemaVersion\":\"0.1.0\",schemaName\":\"htsbundle\",\"READS\":{\"path\":\"myreads" +
                        ".bam\",\"subtype\":\"BAM\"},\"primary\":\"READS\"}",
                   "Invalid JSON near position: 25" },
                // no enclosing {} -> UnsupportedOperationException (no text message)
                {"\"schemaName\":\"htsbundle\", \"schemaVersion\":\"0.1.0\"",
                   "", },
        };
    }

    @Test(dataProvider = "invalidBundleJSON", expectedExceptions = IllegalArgumentException.class)
    public void testFromJSONInvalid(final String jsonString, final String expectedMessageFragment) {
        try {
            new Bundle(jsonString);
        } catch (final Exception e) {
            Assert.assertTrue(e.getMessage().contains(expectedMessageFragment));
            throw e;
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testToJSONNonIOPath() throws IOException {
        try (final InputStream is = new ByteArrayInputStream(new byte[0])) {
            final Bundle bundle = BundleBuilder.start()
                    .addPrimary(new InputStreamResource(is,"displayName","contentType"))
                    .getBundle();
            // can't serialize a resource that isn't backed by an IOPath
            bundle.toJSON();
        } catch (final IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Bundle resource requires a valid path to be serialized"));
            throw e;
        }
    }

    // get the URI String from an IOPath in an IOPath resource
    final private String getURIStringFromIOPath(final IOPathResource resource) {
        return resource.getIOPath().get().getURIString();
    }
}