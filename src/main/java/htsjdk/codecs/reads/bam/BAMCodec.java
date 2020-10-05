package htsjdk.codecs.reads.bam;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsCodec;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.util.FileExtensions;

import java.nio.file.Path;

public abstract class BAMCodec implements ReadsCodec {

    protected static final String BAM_FILE_EXTENSION = FileExtensions.BAM;
    protected static final String BAM_MAGIC = "BAM\1";

    @Override
    public ReadsFormat getFormat() { return ReadsFormat.BAM; }

    @Override
    public String getDisplayName() {
        return String.format("Codec for BAM version %s", getVersion());
    }

    @Override
    public int getFileSignatureSize() {
        return BAM_MAGIC.length();
    }

    @Override
    public boolean canDecode(final IOPath resource) {
        return resource.hasExtension(BAM_FILE_EXTENSION);
    }

    @Override
    public boolean canDecode(final Path path) {
        return path.endsWith(BAM_FILE_EXTENSION);
    }

    // uses a byte array rather than a stream to reduce the need to repeatedly mark/reset the
    // stream for each codec
    @Override
    public boolean canDecode(final byte[] signatureBytes) {
        return signatureBytes.equals("BAM");
    }

}
