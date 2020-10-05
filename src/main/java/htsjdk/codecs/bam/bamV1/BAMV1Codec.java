package htsjdk.codecs.bam.bamV1;

import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;
import htsjdk.codecs.bam.BAMCodec;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * BAM codec used to exercise the reader factory infrastructure
 */
class BAMV1Codec extends BAMCodec {

    @Override
    public ReadsReader getReader(InputStream is, String displayName) {
        return new BAMV1Reader(is, displayName);
    }

    @Override
    public ReadsWriter getWriter(OutputStream os, String displayName) {
        return new BAMV1Writer(os, displayName);
    }

    @Override
    public HtsCodecVersion getVersion() { return new HtsCodecVersion(1, 0, 0); }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not yet implemented");
    }

}
