package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMCodec;
import htsjdk.codecs.reads.cram.cramV3_0.CRAMReaderV3_0;
import htsjdk.codecs.reads.cram.cramV3_0.CRAMWriterV3_0;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * BAM codec used to exercise the reader factory infrastructure
 */
public class CRAMCodecV3_0 extends CRAMCodec {

    protected static final HtsCodecVersion VERSION_3_0 = new HtsCodecVersion(3, 0, 0);

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_3_0;
    }

    @Override
    public ReadsReader getReader(final IOPath inputPath) {
        return new CRAMReaderV3_0(inputPath);
    }

    @Override
    public ReadsReader getReader(InputStream is, String displayName) {
        return new CRAMReaderV3_0(is, displayName);
    }

    @Override
    public ReadsWriter getWriter(final IOPath outputPath) {
        return new CRAMWriterV3_0(outputPath);
    }

    @Override
    public ReadsWriter getWriter(OutputStream os, String displayName) {
        return new CRAMWriterV3_0(os, displayName);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not implemented");
    }

}
