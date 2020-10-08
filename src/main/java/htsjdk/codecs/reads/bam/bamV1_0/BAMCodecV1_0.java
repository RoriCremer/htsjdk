package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;

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
    public int getFileSignatureSize() {
        return BAM_MAGIC.length();
    }

    // uses a byte array rather than a stream to reduce the need to repeatedly mark/reset the
    // stream for each codec
    @Override
    public boolean canDecode(final byte[] signatureBytes) {
        return signatureBytes.equals(BAM_MAGIC);
    }

    @Override
    public ReadsReader getReader(final IOPath inputPath) {
        return new BAMReaderV1_0(inputPath);
    }

    @Override
    public ReadsReader getReader(InputStream is, String displayName) {
        return new BAMReaderV1_0(is, displayName);
    }

    @Override
    public ReadsWriter getWriter(final IOPath outputPath) {
        return new BAMWriterV1_0(outputPath);
    }

    @Override
    public ReadsWriter getWriter(OutputStream os, String displayName) {
        return new BAMWriterV1_0(os, displayName);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not yet implemented");
    }

}
