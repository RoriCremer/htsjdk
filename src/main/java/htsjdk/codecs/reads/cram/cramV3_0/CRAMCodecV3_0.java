package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMCodec;
import htsjdk.codecs.reads.cram.CRAMDecoder;
import htsjdk.codecs.reads.cram.CRAMEncoder;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.HtsDecoder;
import htsjdk.plugin.HtsRecord;
import htsjdk.plugin.bundle.InputBundle;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsEncoderOptions;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.cram.structure.CramHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * CRAM v3.0 codec
 */
public class CRAMCodecV3_0 extends CRAMCodec {
    public static final HtsCodecVersion VERSION_3_0 = new HtsCodecVersion(3, 0, 0);
    protected static final String CRAM_MAGIC = new String(CramHeader.MAGIC) + "\3\0";

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_3_0;
    }

    @Override
    public int getSignatureSize() {
        return CRAM_MAGIC.length();
    }

    // uses a byte array rather than a stream to reduce the need to repeatedly mark/reset the
    // stream for each codec
    @Override
    public boolean canDecodeSignature(final InputStream rawInputStream, final String sourceName) {
        try {
            final byte[] signatureBytes = new byte[getSignatureSize()];
            final int numRead = rawInputStream.read(signatureBytes);
            if (numRead <= 0) {
                throw new HtsjdkIOException(String.format("Failure reading content from stream for %s", sourceName));
            }
            return Arrays.equals(signatureBytes, CRAM_MAGIC.getBytes());
        } catch (IOException e) {
            throw new HtsjdkIOException(String.format("Failure reading content from stream for %s", sourceName));
        }
    }

    @Override
    public CRAMDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, new ReadsDecoderOptions());
    }

    @Override
    public CRAMDecoder getDecoder(final IOPath inputPath, final ReadsDecoderOptions readsDecoderOptions) {
        return new CRAMDecoderV3_0(inputPath, readsDecoderOptions);
    }

    @Override
    public CRAMDecoder getDecoder(final InputStream is, final String displayName) {
        return getDecoder(is, displayName, new ReadsDecoderOptions());
    }

    @Override
    public HtsDecoder<ReadsFormat, ?, ? extends HtsRecord> getDecoder(final InputBundle bundle, final ReadsDecoderOptions decoderOptions) {
        return new CRAMDecoderV3_0(bundle, decoderOptions);
    }

    @Override
    public CRAMDecoder getDecoder(final InputStream is, final String displayName, final ReadsDecoderOptions readsDecoderOptions) {
        return new CRAMDecoderV3_0(is, displayName, readsDecoderOptions);
    }

    @Override
    public CRAMEncoder getEncoder(final IOPath outputPath) {
        return getEncoder(outputPath, new ReadsEncoderOptions());
    }

    @Override
    public CRAMEncoder getEncoder(final IOPath outputPath, final ReadsEncoderOptions readsEncoderOptions) {
        return new CRAMEncoderV3_0(outputPath, readsEncoderOptions);
    }

    @Override
    public CRAMEncoder getEncoder(final OutputStream os, final String displayName) {
        return new CRAMEncoderV3_0(os, displayName);
    }

    @Override
    public CRAMEncoder getEncoder(final OutputStream os, final String displayName, final ReadsEncoderOptions readsEncoderOptions) {
        return new CRAMEncoderV3_0(os, displayName, readsEncoderOptions);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not implemented");
    }

}
