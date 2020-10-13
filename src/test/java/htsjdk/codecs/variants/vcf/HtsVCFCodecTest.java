package htsjdk.codecs.variants.vcf;

import htsjdk.HtsjdkTest;
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
import org.testng.annotations.Test;

public class HtsVCFCodecTest extends HtsjdkTest {
    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/variant/");

    @Test
    public void testRoundTripVCF() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "vcf42HeaderLines.vcf");
        final IOPath outputPath = new HtsPath("pluginVariants.vcf");

        try (final VariantsDecoder variantsDecoder = HtsCodecRegistry.getVariantsDecoder(inputPath);
             final VariantsEncoder variantsEncoder = HtsCodecRegistry.getVariantsEncoder(outputPath)) {

            Assert.assertNotNull(variantsDecoder);
            Assert.assertEquals(variantsDecoder.getFormat(), VariantsFormat.VCF);
            Assert.assertNotNull(variantsEncoder);
            Assert.assertEquals(variantsEncoder.getFormat(), VariantsFormat.VCF);

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
