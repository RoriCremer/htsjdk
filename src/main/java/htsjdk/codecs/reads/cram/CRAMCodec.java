package htsjdk.codecs.reads.cram;

import htsjdk.codecs.hapref.HapRefDecoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecRegistry;
import htsjdk.plugin.reads.ReadsCodec;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.cram.ref.CRAMReferenceSource;
import htsjdk.samtools.cram.ref.ReferenceSource;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import htsjdk.samtools.util.FileExtensions;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class CRAMCodec implements ReadsCodec {
    private final Set<String> extensionMap = new HashSet(Arrays.asList(FileExtensions.CRAM));

    @Override
    public ReadsFormat getFormat() { return ReadsFormat.CRAM; }

    @Override
    public boolean canDecodeExtension(final IOPath ioPath) {
        return extensionMap.stream().anyMatch(ext-> ioPath.hasExtension(ext));
    }

    @Override
    public boolean canDecodeExtension(final Path path) {
        return extensionMap.stream().anyMatch(ext-> path.endsWith(ext));
    }

    public static CRAMReferenceSource getCRAMReferenceSource(final IOPath referencePath) {
        final HapRefDecoder hapRefDecoder = HtsCodecRegistry.getHapRefDecoder(referencePath);
        if (hapRefDecoder == null) {
            throw new RuntimeException(String.format("Unable to get reference codec for %s", referencePath));
        }
        final ReferenceSequenceFile refSeqFile = hapRefDecoder.getRecordReader();
        return new ReferenceSource(refSeqFile);
    }
}
