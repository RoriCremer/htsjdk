package htsjdk.beta.plugin.bundle;

import htsjdk.HtsjdkTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BundleResourceTest extends HtsjdkTest {

    @DataProvider(name="resourceEquality")
    public Object[][] getResourceEquality() {
        return new Object[][]{
                { BundleResourceTestData.inputReadsWithSubType.get(), BundleResourceTestData.inputReadsWithSubType.get(), true },
                { BundleResourceTestData.inputReadsNoSubType.get(), BundleResourceTestData.inputReadsNoSubType.get(), true },

                { BundleResourceTestData.inputReadsWithSubType.get(), BundleResourceTestData.inputReadsNoSubType.get(), false },
        };
    }

    @Test(dataProvider="resourceEquality")
    public void testInputResourceEquality(
            final BundleResource inputResource1,
            final BundleResource inputResource2,
            final boolean expectedEquals) {
        Assert.assertEquals(inputResource1.equals(inputResource2), expectedEquals);
        Assert.assertEquals(inputResource2.equals(inputResource1), expectedEquals);
    }


    @DataProvider(name="toString")
    public Object[][] getToStringData() {
        return new Object[][]{
                // input resources
                { BundleResourceTestData.inputReadsNoSubType.get(), "IOPathResource: READS/NONE" },
        };
    }

    @Test(dataProvider = "toString")
    public void testToString(final BundleResource resource, final String expectedString) {
        System.out.println(resource.toString());
        Assert.assertEquals(resource.toString(), expectedString);
    }
}
