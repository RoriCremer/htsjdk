package htsjdk.beta.codecs.reads.bam.bamV1_0;

import htsjdk.beta.codecs.reads.bam.BAMCodec;
import htsjdk.beta.codecs.reads.bam.BAMDecoder;
import htsjdk.beta.codecs.reads.bam.BAMEncoder;
import htsjdk.beta.plugin.bundle.Bundle;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.beta.plugin.HtsCodecVersion;
import htsjdk.beta.plugin.reads.ReadsDecoderOptions;
import htsjdk.beta.plugin.reads.ReadsEncoderOptions;
import htsjdk.samtools.SamStreams;
import htsjdk.samtools.util.BlockCompressedStreamConstants;
import htsjdk.utils.ValidationUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * BAM codec.
 */
public class BAMCodecV1_0 extends BAMCodec {
    //protected static final String BAM_MAGIC = "BAM\1";
    protected static final HtsCodecVersion VERSION_1 = new HtsCodecVersion(1, 0, 0);

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_1;
    }

    @Override
    public int getSignatureSize() {
        return BlockCompressedStreamConstants.DEFAULT_UNCOMPRESSED_BLOCK_SIZE;
    }

    @Override
    public boolean canDecodeSignature(final InputStream rawInputStream, final String sourceName) {
        ValidationUtils.nonNull(rawInputStream);
        ValidationUtils.nonNull(sourceName);

        try {
            // technically this should check the version, but its BAM so there isn't one...
            return SamStreams.isBAMFile(rawInputStream);
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
    public BAMDecoder getDecoder(final Bundle inputBundle, final ReadsDecoderOptions decoderOptions) {
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
        throw new RuntimeException("Not yet implemented");
    }

}
