package htsjdk.codecs.variants.vcf;

import htsjdk.HtsjdkTest;
import htsjdk.codecs.variants.vcf.vcfv4_2.VCFCodecV4_2;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecRegistry;
import htsjdk.plugin.variants.VariantsDecoder;
import htsjdk.plugin.variants.VariantsEncoder;
import htsjdk.plugin.variants.VariantsFormat;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HtsVCFCodecTest extends HtsjdkTest {
    final IOPath VARIANTS_TEST_DIR = new HtsPath("src/test/resources/htsjdk/variant/");

    @DataProvider(name="vcfTests")
    private Object[][] vcfTests() {
        return new Object[][] {
                // a .vcf, .vcf.gz, .vcf with UTF8 chars, and .vcf.gz with UTF8 chars
                { new HtsPath(VARIANTS_TEST_DIR + "vcf42HeaderLines.vcf") },
                { new HtsPath(VARIANTS_TEST_DIR + "NA12891.vcf.gz") },
        };
    }

    @Test(dataProvider = "vcfTests")
    public void testRoundTripVCF(final IOPath inputPath) {
        final IOPath outputPath = new HtsPath("pluginVariants.vcf");

        try (final VariantsDecoder variantsDecoder = HtsCodecRegistry.getVariantsDecoder(inputPath);
             final VariantsEncoder variantsEncoder = HtsCodecRegistry.getVariantsEncoder(outputPath)) {

            Assert.assertNotNull(variantsDecoder);
            Assert.assertEquals(variantsDecoder.getFormat(), VariantsFormat.VCF);
            Assert.assertEquals(variantsDecoder.getVersion(), VCFCodecV4_2.VCF_V42_VERSION);

            Assert.assertNotNull(variantsEncoder);
            Assert.assertEquals(variantsEncoder.getFormat(), VariantsFormat.VCF);
            Assert.assertEquals(variantsEncoder.getVersion(), VCFCodecV4_2.VCF_V42_VERSION);

            final VCFReader vcfReader = variantsDecoder.getRecordReader();
            Assert.assertNotNull(vcfReader);

            final VCFHeader vcfHeader = variantsDecoder.getHeader();
            Assert.assertNotNull(vcfHeader);

            final VariantContextWriter vcfWriter = variantsEncoder.getRecordWriter(vcfHeader);
            vcfWriter.writeHeader(vcfHeader);
            for (final VariantContext vc : vcfReader) {
                vcfWriter.add(vc);
            }
        }
    }
}
