package htsjdk.codecs.variants.vcf;

import htsjdk.io.IOPath;
import htsjdk.plugin.variants.VariantsCodec;
import htsjdk.plugin.variants.VariantsFormat;
import htsjdk.samtools.util.FileExtensions;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public abstract class VCFCodec implements VariantsCodec {

    //TODO: FileExtensions.VCF_LIST includes BCF!
    private final Set<String> extensionMap = new HashSet(FileExtensions.VCF_LIST);

    @Override
    public VariantsFormat getFormat() { return VariantsFormat.VCF; }

    @Override
    public boolean canDecodeExtension(final IOPath ioPath) {
        return extensionMap.stream().anyMatch(ext-> ioPath.hasExtension(ext));
    }

    @Override
    public boolean canDecodeExtension(final Path path) {
        return extensionMap.stream().anyMatch(ext-> path.endsWith(ext));
    }
}
