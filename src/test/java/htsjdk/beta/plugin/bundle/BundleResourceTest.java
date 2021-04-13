package htsjdk.beta.plugin.bundle;

import htsjdk.HtsjdkTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BundleResourceTest extends HtsjdkTest {

    @DataProvider(name="inputOutputTestData")
    public Object[][] getInputOutputTestData() {
        return new Object[][]{
                // bundle resource, isInput, isOutput
                { BundleResourceTestData.readsWithSubContentType, true, true},
                { new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName", BundleResourceType.READS), true, false},
                { new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName", BundleResourceType.READS), false, true},
        };
    }

    @Test(dataProvider = "inputOutputTestData")
    public void testIsInputOutput(final BundleResource resource, final boolean expectedIsInput, final boolean expectedIsOutput) {
        Assert.assertEquals(resource.isInputResource(), expectedIsInput);
        Assert.assertEquals(resource.isOutputResource(), expectedIsOutput);
    }

    @DataProvider(name="resourceEqualityTestData")
    public Object[][] getResourceEqualityTestData() {
        return new Object[][]{

                // equal
                {
                        BundleResourceTestData.readsWithSubContentType,
                        BundleResourceTestData.readsWithSubContentType,
                        true
                },
                {
                        BundleResourceTestData.readsNoSubContentType,
                        BundleResourceTestData.readsNoSubContentType,
                        true
                },
                {
                        new IOPathResource(BundleResourceTestData.READS_FILE, BundleResourceType.READS),
                        new IOPathResource(BundleResourceTestData.READS_FILE, BundleResourceType.READS),
                        true
                },
                {
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName",
                                BundleResourceType.READS),
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName",
                                BundleResourceType.READS),
                        true
                },
                {
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName",
                                BundleResourceType.READS),
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName",
                                BundleResourceType.READS),
                        true
                },

                // not equal
                {
                        new IOPathResource(BundleResourceTestData.READS_FILE, BundleResourceType.READS),
                        new IOPathResource(BundleResourceTestData.READS_FILE, "NOTREADS"),
                        false
                },
                {
                        BundleResourceTestData.readsWithSubContentType,
                        BundleResourceTestData.readsNoSubContentType,
                        false
                },
                {
                        BundleResourceTestData.indexWithSubContentType,
                        BundleResourceTestData.readsNoSubContentType,
                        false
                },

                // not equal inputstreams
                {
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName",
                                BundleResourceType.READS),
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "differentDisplayName",
                                BundleResourceType.READS),
                        false
                },
                {
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName",
                                BundleResourceType.READS, BundleResourceType.READS_BAM),
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName",
                                BundleResourceType.READS),
                        false
                },
                {
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName",
                                BundleResourceType.READS, BundleResourceType.READS_BAM),
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName",
                                BundleResourceType.READS, BundleResourceType.READS_CRAM),
                        false
                },
                {
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName",
                                BundleResourceType.READS),
                        new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName",
                                BundleResourceType.VARIANTS),
                        false
                },

                // not equal outputstreams
                {
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName",
                                BundleResourceType.READS),
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "differentDisplayName",
                                BundleResourceType.READS),
                        false
                },
                {
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName",
                                BundleResourceType.READS, BundleResourceType.READS_BAM),
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName",
                                BundleResourceType.READS),
                        false
                },
                {
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName",
                                BundleResourceType.READS, BundleResourceType.READS_BAM),
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName",
                                BundleResourceType.READS, BundleResourceType.READS_CRAM),
                        false
                },
                {
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName",
                                BundleResourceType.READS),
                        new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName",
                                BundleResourceType.VARIANTS),
                        false
                },
        };
    }

    @Test(dataProvider="resourceEqualityTestData")
    public void testInputResourceEquality(
            final BundleResource inputResource1,
            final BundleResource inputResource2,
            final boolean expectedEquals) {
        Assert.assertEquals(inputResource1.equals(inputResource2), expectedEquals);
        Assert.assertEquals(inputResource2.equals(inputResource1), expectedEquals);
    }

    @DataProvider(name="toStringTestData")
    public Object[][] getToStringTestData() {
        return new Object[][]{
                {BundleResourceTestData.readsWithSubContentType, "IOPathResource (myreads.bam): READS/BAM"},
                {BundleResourceTestData.readsNoSubContentType, "IOPathResource (myreads.bam): READS/NONE"},
                {BundleResourceTestData.indexNoSubContentType, "IOPathResource (myreads.bai): READS_INDEX/NONE"},
                {BundleResourceTestData.indexWithSubContentType, "IOPathResource (myreads.bai): READS_INDEX/BAI"},
                {new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName", BundleResourceType.READS),
                        "InputStreamResource (displayName): READS/NONE"},
                {new InputStreamResource(BundleResourceTestData.fakeInputStream, "displayName", BundleResourceType.READS, BundleResourceType.READS_BAM),
                        "InputStreamResource (displayName): READS/BAM"},
                {new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName", BundleResourceType.READS),
                        "OutputStreamResource (displayName): READS/NONE"},
                {new OutputStreamResource(BundleResourceTestData.fakeOutputStream, "displayName", BundleResourceType.READS, BundleResourceType.READS_BAM),
                        "OutputStreamResource (displayName): READS/BAM"},
        };
    }

    @Test(dataProvider = "toStringTestData")
    public void testToString(final BundleResource resource, final String expectedString) {
        Assert.assertTrue(resource.toString().contains(expectedString));
    }

}
