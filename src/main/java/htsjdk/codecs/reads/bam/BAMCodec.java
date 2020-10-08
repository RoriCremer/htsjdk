package htsjdk.codecs.reads.bam;

import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsCodec;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.util.FileExtensions;

import java.nio.file.Path;

public abstract class BAMCodec implements ReadsCodec {

    public static HtsCodecVersion BAM_DEFAULT_VERSION = new HtsCodecVersion(1, 0,0);

    protected static final String BAM_FILE_EXTENSION = FileExtensions.BAM;

    @Override
    public ReadsFormat getFormat() { return ReadsFormat.BAM; }

    //TODO: this needs to be permissive of multiple extensions
    @Override
    public boolean canDecode(final IOPath ioPath) {
        return ioPath.hasExtension(BAM_FILE_EXTENSION);
    }

    @Override
    public boolean canDecode(final Path path) {
        return path.endsWith(BAM_FILE_EXTENSION);
    }

}
