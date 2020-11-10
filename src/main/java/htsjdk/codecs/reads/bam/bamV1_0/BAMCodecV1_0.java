package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMCodec;
import htsjdk.codecs.reads.bam.BAMDecoder;
import htsjdk.codecs.reads.bam.BAMEncoder;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsBundle;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsEncoderOptions;
import htsjdk.samtools.SamStreams;
import htsjdk.utils.ValidationUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
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
    public boolean canDecodeSignature(final InputStream rawInputStream, final String sourceName) {
        ValidationUtils.nonNull(rawInputStream);
        ValidationUtils.nonNull(sourceName);

        try (final BufferedInputStream bis = new BufferedInputStream(rawInputStream)) {
            // technically this should check the version, but its BAM so there isn't one
            return SamStreams.isBAMFile(bis);
        } catch (IOException e) {
            throw new HtsjdkIOException(String.format("Failure reading signature from stream for %s", sourceName), e);
        }
    }

    @Override
    public BAMDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, new ReadsDecoderOptions());
    }

    @Override
    public BAMDecoder getDecoder(final IOPath inputPath, final ReadsDecoderOptions decoderOptions) {
        return new BAMDecoderV1_0(inputPath, decoderOptions);
    }

    @Override
    public BAMDecoder getDecoder(final ReadsBundle inputBundle, final ReadsDecoderOptions decoderOptions) {
        return new BAMDecoderV1_0(inputBundle, decoderOptions);
    }

    @Override
    public BAMDecoder getDecoder(final InputStream is, final String displayName) {
        return getDecoder(is, displayName, new ReadsDecoderOptions());
    }

    @Override
    public BAMDecoder getDecoder(final InputStream is, final String displayName, final ReadsDecoderOptions decoderOptions) {
        return new BAMDecoderV1_0(is, displayName, decoderOptions);
    }

    @Override
    public BAMEncoder getEncoder(final IOPath outputPath) {
        return new BAMEncoderV1_0(outputPath);
    }

    @Override
    public BAMEncoder getEncoder(final IOPath outputPath, final ReadsEncoderOptions encoderOptions) {
        return new BAMEncoderV1_0(outputPath, encoderOptions);
    }

    @Override
    public BAMEncoder getEncoder(final OutputStream os, final String displayName) {
        return new BAMEncoderV1_0(os, displayName);
    }

    @Override
    public BAMEncoder getEncoder(final OutputStream os, final String displayName, final ReadsEncoderOptions encoderOptions) {
        return new BAMEncoderV1_0(os, displayName, encoderOptions);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not yet implemented");
    }

}
