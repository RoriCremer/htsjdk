package htsjdk.beta.codecs.variants.vcf.vcfv4_2;

import htsjdk.beta.codecs.variants.vcf.VCFEncoder;
import htsjdk.io.IOPath;
import htsjdk.beta.plugin.HtsCodecVersion;
import htsjdk.beta.plugin.variants.VariantsEncoderOptions;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.vcf.VCFHeader;

import java.io.OutputStream;

import static htsjdk.variant.variantcontext.writer.Options.INDEX_ON_THE_FLY;

public class VCFEncoderV4_2 extends VCFEncoder {
    private final VariantsEncoderOptions variantsEncoderOptions;
    private VariantContextWriter vcfWriter;

    public VCFEncoderV4_2(final IOPath outputPath) {
        super(outputPath);
        this.variantsEncoderOptions = new VariantsEncoderOptions();
    }

    public VCFEncoderV4_2(final IOPath outputPath, final VariantsEncoderOptions variantsEncoderOptions) {
        super(outputPath);
        this.variantsEncoderOptions = variantsEncoderOptions;
    }

    public VCFEncoderV4_2(final OutputStream os, final String displayName) {
        super(os, displayName);
        this.variantsEncoderOptions = new VariantsEncoderOptions();
    }

    public VCFEncoderV4_2(final OutputStream os, final String displayName, final VariantsEncoderOptions readsEncoderOptions) {
        super(os, displayName);
        this.variantsEncoderOptions = readsEncoderOptions;
    }

    @Override
    public HtsCodecVersion getVersion() {
        return VCFCodecV4_2.VCF_V42_VERSION;
    }

    @Override
    public void setHeader(final VCFHeader vcfHeader) {
        vcfWriter = getVCFWriter(variantsEncoderOptions, vcfHeader);
        vcfWriter.writeHeader(vcfHeader);
    }

    @Override
    public void write(final VariantContext variantContext) {
        vcfWriter.add(variantContext);
    }

    @Override
    public void close() {
        if (vcfWriter != null) {
            vcfWriter.close();
        }
    }

    private VariantContextWriter getVCFWriter(final VariantsEncoderOptions variantsEncoderOptions, final VCFHeader vcfHeader) {
        if (os != null) {
            throw new IllegalArgumentException("VCF writer to stream not yet implemented");
        } else {
            final VariantContextWriterBuilder builder = new VariantContextWriterBuilder();
            return builder
                    .setOutputPath(outputPath.toPath())
                    .unsetOption(INDEX_ON_THE_FLY)
                    .build();
        }
    }
}
