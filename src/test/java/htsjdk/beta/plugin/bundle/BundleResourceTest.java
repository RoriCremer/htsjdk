package htsjdk.beta.plugin.bundle;

import htsjdk.HtsjdkTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BundleResourceTest extends HtsjdkTest {

    @Test
    public void testInputOutputResourceInequality() {
        final InputResource inputResource = new InputIOPathResource(
                BundleResourceTestData.READS_FILE,
                BundleResourceType.READS,
                BundleResourceType.READS_BAM);

        final OutputResource outputResource = new OutputIOPathResource(
                BundleResourceTestData.READS_FILE,
                BundleResourceType.READS,
                BundleResourceType.READS_BAM);

        Assert.assertNotEquals(inputResource, outputResource);
    }

    @DataProvider(name="toString")
    public Object[][] getToStringData() {
        return new Object[][]{
                // input resources
                { BundleResourceTestData.inputReadsNoSubType.get(), "InputIOPathResource: READS/NONE" },
                { BundleResourceTestData.inputReadsWithTag.get(), "InputIOPathResource: READS/BAM testTAG" },
                { BundleResourceTestData.inputReadsWithTagOneAttribute.get(),
                        "InputIOPathResource: READS/BAM testTAG attribute1:value1" },
                { BundleResourceTestData.inputReadsWithTagTwoAttributes.get(),
                        "InputIOPathResource: READS/BAM testTAG attribute1:value1 attribute2:value2" },

                // output resources
                { BundleResourceTestData.outputReadsNoSubType.get(), "OutputIOPathResource: READS/NONE" },
                { BundleResourceTestData.outputReadsWithTag.get(), "OutputIOPathResource: READS/BAM testTAG" },
                { BundleResourceTestData.outputReadsWithTagOneAttribute.get(),
                        "OutputIOPathResource: READS/BAM testTAG attribute1:value1" },
                { BundleResourceTestData.outputReadsWithTagTwoAttributes.get(),
                        "OutputIOPathResource: READS/BAM testTAG attribute1:value1 attribute2:value2" }
        };
    }

    @Test(dataProvider = "toString")
    public void testToString(final BundleResource resource, final String expectedString) {
        System.out.println(resource.toString());
        Assert.assertEquals(resource.toString(), expectedString);
    }
}
