package htsjdk.beta.plugin.reads;

import htsjdk.HtsjdkTest;
import htsjdk.beta.plugin.IOUtils;
import htsjdk.beta.plugin.bundle.Bundle;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ReadsBundleTest extends HtsjdkTest {

    private final static String BAM_FILE = "reads.bam";
    private final static String INDEX_FILE = "reads.bai";

    @Test
    public void testReadsBundleReadsOnly() {
        final IOPath readsPath = new HtsPath(BAM_FILE);
        final ReadsBundle readsBundle = new ReadsBundle(readsPath);

        Assert.assertEquals(readsBundle.getReads(), readsPath);
        Assert.assertFalse(readsBundle.getIndex().isPresent());
    }

    @Test
    public void testReadsBundleReadsIndex() {
        final IOPath readsPath = new HtsPath(BAM_FILE);
        final IOPath indexPath = new HtsPath(INDEX_FILE);
        final ReadsBundle readsBundle = new ReadsBundle(readsPath, indexPath);

        Assert.assertEquals(readsBundle.getReads(), readsPath);
        Assert.assertTrue(readsBundle.getIndex().isPresent());
        Assert.assertEquals(readsBundle.getIndex().get(), indexPath);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testNoReadsInSerializedBundle() {
        final String vcfJSON = "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"VARIANTS\":{\"path\":\"my.vcf\",\"subtype\":\"VCF\"},\"primary\":\"VARIANTS\"}";
        try {
            new ReadsBundle(vcfJSON);
        } catch (final RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains(ReadsBundle.READS_MISSING));
            throw e;
        }
    }

    @DataProvider(name = "roundTripJSONTestData")
    public Object[][] getRoundTripJSONTestData() {
        return new Object[][]{
                //NOTE that these JSON strings contain the resources in the same order that they're serialized by mjson
                // so that we can use these cases to validate in both directions

                // json string, primary key, corresponding array of resources
                {
                    "{\"schemaVersion\":\"0.1.0\",\"schemaName\":\"htsbundle\",\"READS\":{\"path\":\"" + BAM_FILE + "\",\"subtype\":\"BAM\"},\"primary\":\"READS\"}",
                    new ReadsBundle(new HtsPath(BAM_FILE))
                },
        };
    }

    @Test(dataProvider="roundTripJSONTestData")
    public void testReadWriteRoundTrip(
            final String jsonString,
            final ReadsBundle bundle)  {
        final ReadsBundle bundleFromJSON = new ReadsBundle(jsonString);
        Assert.assertEquals(bundleFromJSON, bundle);
        Assert.assertEquals(bundleFromJSON.getPrimaryResourceKey(), bundle.getPrimaryResourceKey());
        Assert.assertEquals(bundleFromJSON.getReads(), bundle.getReads());
    }

    @Test(dataProvider="roundTripJSONTestData")
    public void testGetReadsBundleFromPath(
            final String jsonString,
            final ReadsBundle bundle)  {
        final IOPath jsonFilePath = IOUtils.createTempPath("reads", Bundle.BUNDLE_EXTENSION);
        IOUtils.writeStringToPath(jsonFilePath, jsonString, false);
        final ReadsBundle bundleFromPath = ReadsBundle.getReadsBundleFromPath(jsonFilePath);

        Assert.assertEquals(bundleFromPath, bundle);
    }

}