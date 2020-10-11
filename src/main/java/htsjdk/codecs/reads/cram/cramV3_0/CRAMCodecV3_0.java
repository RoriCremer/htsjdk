package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsEncoder;
import htsjdk.samtools.SAMFileWriterFactory;
import htsjdk.samtools.SamReaderFactory;

import java.io.InputStream;
import java.io.OutputStream;

// TODO: This should reject CRAM 3.1
/**
 * BAM codec used to exercise the reader factory infrastructure
 */
public class CRAMCodecV3_0 extends CRAMCodec {

    public static final HtsCodecVersion VERSION_3_0 = new HtsCodecVersion(3, 0, 0);

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_3_0;
    }

    @Override
    public int getSignatureSize() {
        return CRAM_MAGIC.length();
    }

    @Override
    public ReadsDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, SamReaderFactory.makeDefault());
    }

    @Override
    public ReadsDecoder getDecoder(final IOPath inputPath, final SamReaderFactory samReaderFactory) {
        return new CRAMDecoderV3_0(inputPath, samReaderFactory);
    }

    @Override
    public ReadsDecoder getDecoder(final InputStream is, final String displayName) {
        return getDecoder(is, displayName, SamReaderFactory.makeDefault());
    }

    @Override
    public ReadsDecoder getDecoder(final InputStream is, final String displayName, final SamReaderFactory samReaderFactory) {
        return new CRAMDecoderV3_0(is, displayName, samReaderFactory);
    }

    @Override
    public ReadsEncoder getEncoder(final IOPath outputPath) {
        return getEncoder(outputPath, new SAMFileWriterFactory());
    }

    @Override
    public ReadsEncoder getEncoder(final IOPath outputPath, final SAMFileWriterFactory samFileWriterFactory) {
        return new CRAMEncoderV3_0(outputPath, samFileWriterFactory);
    }

    @Override
    public ReadsEncoder getEncoder(final OutputStream os, final String displayName) {
        return new CRAMEncoderV3_0(os, displayName);
    }

    @Override
    public ReadsEncoder getEncoder(final OutputStream os, final String displayName, final SAMFileWriterFactory samFileWriterFactory) {
        return new CRAMEncoderV3_0(os, displayName, samFileWriterFactory);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not implemented");
    }

}
