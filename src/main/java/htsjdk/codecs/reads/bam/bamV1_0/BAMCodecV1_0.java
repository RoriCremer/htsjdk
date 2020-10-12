package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsEncoder;
import htsjdk.plugin.reads.ReadsEncoderOptions;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * BAM codec used to exercise the reader factory infrastructure
 */
public class BAMCodecV1_0 extends BAMCodec {
    protected static final String BAM_MAGIC = "BAM\1";

    protected static final HtsCodecVersion VERSION_1 = new HtsCodecVersion(1, 0, 0);

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_1;
    }

    @Override
    public int getSignatureSize() {
        return BAM_MAGIC.length();
    }

    // uses a byte array rather than a stream to reduce the need to repeatedly mark/reset the
    // stream for each codec
    @Override
    public boolean canDecodeSignature(final byte[] signatureBytes) {
        return signatureBytes.equals(BAM_MAGIC);
    }

    @Override
    public ReadsDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, new ReadsDecoderOptions());
    }

    @Override
    public ReadsDecoder getDecoder(final IOPath inputPath, final ReadsDecoderOptions decoderOptions) {
        return new BAMDecoderV1_0(inputPath, decoderOptions);
    }

    @Override
    public ReadsDecoder getDecoder(final InputStream is, final String displayName) {
        return getDecoder(is, displayName, new ReadsDecoderOptions());
    }

    @Override
    public ReadsDecoder getDecoder(final InputStream is, final String displayName, final ReadsDecoderOptions decoderOptions) {
        return new BAMDecoderV1_0(is, displayName, decoderOptions);
    }

    @Override
    public ReadsEncoder getEncoder(final IOPath outputPath) {
        return new BAMEncoderV1_0(outputPath);
    }

    @Override
    public ReadsEncoder getEncoder(final IOPath outputPath, final ReadsEncoderOptions encoderOptions) {
        return new BAMEncoderV1_0(outputPath, encoderOptions);
    }

    @Override
    public ReadsEncoder getEncoder(final OutputStream os, final String displayName) {
        return new BAMEncoderV1_0(os, displayName);
    }

    @Override
    public ReadsEncoder getEncoder(final OutputStream os, final String displayName, final ReadsEncoderOptions encoderOptions) {
        return new BAMEncoderV1_0(os, displayName, encoderOptions);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not yet implemented");
    }

}
