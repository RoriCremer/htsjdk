package htsjdk.plugin.bundle;

import htsjdk.HtsjdkTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class InputBundleResourceTest extends HtsjdkTest {

    @DataProvider(name="inputResourceEquality")
    public Object[][] getInputResourceEquality() {
        return new Object[][]{
                { BundleResourceTestData.inputReadsWithSubType.get(), BundleResourceTestData.inputReadsWithSubType.get(), true },
                { BundleResourceTestData.inputReadsNoSubType.get(), BundleResourceTestData.inputReadsNoSubType.get(), true },
                { BundleResourceTestData.inputReadsWithTag.get(), BundleResourceTestData.inputReadsWithTag.get(), true },
                { BundleResourceTestData.inputReadsWithTagOneAttribute.get(), BundleResourceTestData.inputReadsWithTagOneAttribute.get(), true },
                { BundleResourceTestData.inputReadsWithTagTwoAttributes.get(), BundleResourceTestData.inputReadsWithTagTwoAttributes.get(), true },
                { BundleResourceTestData.inputReadsNoTagTwoAttributes.get(), BundleResourceTestData.inputReadsNoTagTwoAttributes.get(), true },

                { BundleResourceTestData.inputReadsWithSubType.get(), BundleResourceTestData.inputReadsNoSubType.get(), false },
                { BundleResourceTestData.inputReadsWithSubType.get(), BundleResourceTestData.inputReadsWithTag.get(), false },
                { BundleResourceTestData.inputReadsWithTag.get(), BundleResourceTestData.inputReadsWithTagOneAttribute.get(), false },
                { BundleResourceTestData.inputReadsWithTagOneAttribute.get(), BundleResourceTestData.inputReadsWithTagTwoAttributes.get(), false },
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

}
