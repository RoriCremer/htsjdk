package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;
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
    public ReadsReader getReader(final IOPath inputPath) {
        return getReader(inputPath, SamReaderFactory.makeDefault());
    }

    @Override
    public ReadsReader getReader(final IOPath inputPath, final SamReaderFactory samReaderFactory) {
        return new CRAMReaderV3_0(inputPath, samReaderFactory);
    }

    @Override
    public ReadsReader getReader(final InputStream is, final String displayName) {
        return getReader(is, displayName, SamReaderFactory.makeDefault());
    }

    @Override
    public ReadsReader getReader(final InputStream is, final String displayName, final SamReaderFactory samReaderFactory) {
        return new CRAMReaderV3_0(is, displayName, samReaderFactory);
    }

    @Override
    public ReadsWriter getWriter(final IOPath outputPath) {
        return getWriter(outputPath, new SAMFileWriterFactory());
    }

    @Override
    public ReadsWriter getWriter(final IOPath outputPath, final SAMFileWriterFactory samFileWriterFactory) {
        return new CRAMWriterV3_0(outputPath, samFileWriterFactory);
    }

    @Override
    public ReadsWriter getWriter(final OutputStream os, final String displayName) {
        return new CRAMWriterV3_0(os, displayName);
    }

    @Override
    public ReadsWriter getWriter(final OutputStream os, final String displayName, final SAMFileWriterFactory samFileWriterFactory) {
        return new CRAMWriterV3_0(os, displayName, samFileWriterFactory);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not implemented");
    }

}
