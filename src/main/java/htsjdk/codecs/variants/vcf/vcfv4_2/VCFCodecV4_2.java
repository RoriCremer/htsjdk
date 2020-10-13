package htsjdk.codecs.variants.vcf.vcfv4_2;

import htsjdk.codecs.variants.vcf.VCFCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.variants.VariantsDecoder;
import htsjdk.plugin.variants.VariantsDecoderOptions;
import htsjdk.plugin.variants.VariantsEncoder;
import htsjdk.plugin.variants.VariantsEncoderOptions;

import java.io.InputStream;
import java.io.OutputStream;

public class VCFCodecV4_2 extends VCFCodec {
    public static HtsCodecVersion VCF_V42_VERSION = new HtsCodecVersion(4, 2,0);
    protected static final String VCF_V42_MAGIC = "##fileformat=VCFv42";

    @Override
    public HtsCodecVersion getVersion() { return VCF_V42_VERSION; }

    @Override
    public int getSignatureSize() {
        return VCF_V42_MAGIC.length();
    }

    // uses a byte array rather than a stream to reduce the need to repeatedly mark/reset the
    // stream for each codec
    @Override
    public boolean canDecodeSignature(final byte[] signatureBytes) {
        return signatureBytes.equals(VCF_V42_MAGIC);
    }

    @Override
    public VariantsDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, new VariantsDecoderOptions());
    }

    @Override
    public VariantsDecoder getDecoder(final IOPath inputPath, final VariantsDecoderOptions decoderOptions) {
        return new VCFDecoderV4_2(inputPath, decoderOptions);
    }

    @Override
    public VariantsDecoder getDecoder(final InputStream is, final String displayName) {
        return getDecoder(is, displayName, new VariantsDecoderOptions());
    }

    @Override
    public VariantsDecoder getDecoder(final InputStream is, final String displayName, final VariantsDecoderOptions decoderOptions) {
        return new VCFDecoderV4_2(is, displayName, decoderOptions);
    }

    @Override
    public VariantsEncoder getEncoder(final IOPath outputPath) {
        return new VCFEncoderV4_2(outputPath);
    }

    @Override
    public VariantsEncoder getEncoder(final IOPath outputPath, final VariantsEncoderOptions encoderOptions) {
        return new VCFEncoderV4_2(outputPath, encoderOptions);
    }

    @Override
    public VariantsEncoder getEncoder(final OutputStream os, final String displayName) {
        return new VCFEncoderV4_2(os, displayName);
    }

    @Override
    public VariantsEncoder getEncoder(final OutputStream os, final String displayName, final VariantsEncoderOptions encoderOptions) {
        return new VCFEncoderV4_2(os, displayName, encoderOptions);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not yet implemented");
    }

}
