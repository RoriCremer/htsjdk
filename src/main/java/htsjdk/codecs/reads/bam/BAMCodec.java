package htsjdk.codecs.reads.bam;

import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsCodec;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.util.FileExtensions;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for BAM codecs.
 */
public abstract class BAMCodec implements ReadsCodec {
    public static HtsCodecVersion BAM_DEFAULT_VERSION = new HtsCodecVersion(1, 0,0);

    private static final Set<String> extensionMap = new HashSet(Arrays.asList(FileExtensions.BAM));

    @Override
    public ReadsFormat getFileFormat() { return ReadsFormat.BAM; }

    @Override
    public boolean canDecodeURI(final IOPath ioPath) {
        return ioPath.getScheme().equals("file") && extensionMap.stream().anyMatch(ext-> ioPath.hasExtension(ext));
    }

    @Override
    public boolean canDecodeExtension(final Path path) {
        return extensionMap.stream().anyMatch(ext-> path.endsWith(ext));
    }

}
