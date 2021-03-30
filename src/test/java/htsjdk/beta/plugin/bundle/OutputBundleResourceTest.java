package htsjdk.beta.plugin.bundle;

import htsjdk.HtsjdkTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OutputBundleResourceTest extends HtsjdkTest {

    @DataProvider(name="outputResourceEquality")
    public Object[][] getOutputResourceEquality() {
        return new Object[][]{
                { BundleResourceTestData.outputReadsWithSubType.get(), BundleResourceTestData.outputReadsWithSubType.get(), true },
                { BundleResourceTestData.outputReadsWithTag.get(), BundleResourceTestData.outputReadsWithTag.get(), true },
                { BundleResourceTestData.outputReadsWithTagOneAttribute.get(), BundleResourceTestData.outputReadsWithTagOneAttribute.get(), true },
                { BundleResourceTestData.outputReadsWithTagTwoAttributes.get(), BundleResourceTestData.outputReadsWithTagTwoAttributes.get(), true },

                { BundleResourceTestData.outputReadsWithSubType.get(), BundleResourceTestData.outputReadsNoSubType.get(), false },
                { BundleResourceTestData.outputReadsWithSubType.get(), BundleResourceTestData.outputReadsWithTag.get(), false },
                { BundleResourceTestData.outputReadsWithTagTwoAttributes.get(), BundleResourceTestData.outputReadsWithTagOneAttribute.get(), false },
                { BundleResourceTestData.outputReadsWithSubType.get(), BundleResourceTestData.outputReadsWithTagTwoAttributes.get(), false },
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

}
