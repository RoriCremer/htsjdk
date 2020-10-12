package htsjdk.codecs.hapref;

import htsjdk.io.IOPath;
import htsjdk.plugin.hapref.HaploidReferenceCodec;
import htsjdk.samtools.util.FileExtensions;

import java.nio.file.Path;

public abstract class HapRefCodec implements HaploidReferenceCodec {

    @Override
    public boolean canDecodeExtension(final IOPath ioPath) {
        return FileExtensions.FASTA.stream().anyMatch(ext-> ioPath.hasExtension(ext));
    }

    @Override
    public boolean canDecodeExtension(final Path path) {
        return FileExtensions.FASTA.stream().anyMatch(ext-> path.endsWith(ext));
    }

}
