package htsjdk.codecs.reads.cram;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsCodec;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.util.FileExtensions;

import java.nio.file.Path;

public abstract class CRAMCodec implements ReadsCodec {

    protected static final String CRAM_FILE_EXTENSION = FileExtensions.CRAM;
    protected static final String CRAM_MAGIC = "CRAM\1";

    @Override
    public ReadsFormat getFormat() { return ReadsFormat.CRAM; }

    @Override
    public int getFileSignatureSize() {
        return CRAM_MAGIC.length();
    }

    @Override
    public boolean canDecode(final IOPath resource) {
        return resource.hasExtension(CRAM_FILE_EXTENSION);
    }

    @Override
    public boolean canDecode(final Path path) {
        return path.endsWith(CRAM_FILE_EXTENSION);
    }

    // uses a byte array rather than a stream to reduce the need to repeatedly mark/reset the
    // stream for each codec
    @Override
    public boolean canDecode(final byte[] signatureBytes) {
        return signatureBytes.equals("CRAM");
    }

}
