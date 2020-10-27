package htsjdk.codecs.variants.vcf.vcfv4_2;

import htsjdk.codecs.variants.vcf.VCFEncoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.HtsEncoderOptions;
import htsjdk.plugin.variants.VariantsHtsEncoderOptions;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.vcf.VCFHeader;

import java.io.OutputStream;

import static htsjdk.variant.variantcontext.writer.Options.INDEX_ON_THE_FLY;

public class VCFEncoderV4_2 extends VCFEncoder {
    private final HtsEncoderOptions htsEncoderOptions;
    private VariantContextWriter vcfWriter;

    public VCFEncoderV4_2(final IOPath outputPath) {
        super(outputPath);
        this.htsEncoderOptions = new VariantsHtsEncoderOptions();
    }

    public VCFEncoderV4_2(final IOPath outputPath, final HtsEncoderOptions htsEncoderOptions) {
        super(outputPath);
        this.htsEncoderOptions = htsEncoderOptions;
    }

    public VCFEncoderV4_2(final OutputStream os, final String displayName) {
        super(os, displayName);
        this.htsEncoderOptions = new VariantsHtsEncoderOptions();
    }

    public VCFEncoderV4_2(final OutputStream os, final String displayName, final HtsEncoderOptions readsEncoderOptions) {
        super(os, displayName);
        this.htsEncoderOptions = readsEncoderOptions;
    }

    @Override
    public HtsCodecVersion getVersion() {
        return VCFCodecV4_2.VCF_V42_VERSION;
    }

    @Override
    public void setHeader(final VCFHeader vcfHeader) {
        vcfWriter = getVCFWriter(htsEncoderOptions, vcfHeader);
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

    private VariantContextWriter getVCFWriter(final HtsEncoderOptions htsEncoderOptions, final VCFHeader vcfHeader) {
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
