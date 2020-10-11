package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsEncoder;
import htsjdk.samtools.SAMFileWriterFactory;
import htsjdk.samtools.SamReaderFactory;

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
        return getDecoder(inputPath, SamReaderFactory.makeDefault());
    }

    @Override
    public ReadsDecoder getDecoder(final IOPath inputPath, final SamReaderFactory samReaderFactory) {
        return new BAMDecoderV1_0(inputPath, samReaderFactory);
    }

    @Override
    public ReadsDecoder getDecoder(final InputStream is, final String displayName) {
        return getDecoder(is, displayName, SamReaderFactory.makeDefault());
    }

    @Override
    public ReadsDecoder getDecoder(final InputStream is, final String displayName, final SamReaderFactory samReaderFactory) {
        return new BAMDecoderV1_0(is, displayName, samReaderFactory);
    }

    @Override
    public ReadsEncoder getEncoder(final IOPath outputPath) {
        return new BAMEncoderV1_0(outputPath);
    }

    @Override
    public ReadsEncoder getEncoder(final IOPath outputPath, final SAMFileWriterFactory samFileWriterFactory) {
        return new BAMEncoderV1_0(outputPath, samFileWriterFactory);
    }

    @Override
    public ReadsEncoder getEncoder(final OutputStream os, final String displayName) {
        return new BAMEncoderV1_0(os, displayName);
    }

    @Override
    public ReadsEncoder getEncoder(final OutputStream os, final String displayName, final SAMFileWriterFactory samFileWriterFactory) {
        return new BAMEncoderV1_0(os, displayName, samFileWriterFactory);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not yet implemented");
    }

}
