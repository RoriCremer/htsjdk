package htsjdk.codecs.reads.cram;

import htsjdk.codecs.hapref.HapRefDecoder;
import htsjdk.codecs.hapref.fasta.FASTACodecV1_0;
import htsjdk.codecs.hapref.fasta.FASTADecoderV1_0;
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
    public ReadsFormat getFileFormat() { return ReadsFormat.CRAM; }

    @Override
    public boolean canDecodeExtension(final IOPath ioPath) {
        return extensionMap.stream().anyMatch(ext-> ioPath.hasExtension(ext));
    }

    @Override
    public boolean canDecodeExtension(final Path path) {
        return extensionMap.stream().anyMatch(ext-> path.endsWith(ext));
    }

    public static CRAMReferenceSource getCRAMReferenceSource(final IOPath referencePath) {
        //TODO: this cast can't be required...its necessary because the generic decoder interface
        // is an iterable<ReferenceSequence>, but we need the native (indexed by contig) interface
        // implemented on ReferenceSequenceFile, so we need to cast the decoder in order to get
        // access to the ReferenceSequenceFile
        final FASTADecoderV1_0 fastaV1Decoder = (FASTADecoderV1_0) HtsCodecRegistry.getHapRefDecoder(referencePath);
        if (fastaV1Decoder == null) {
            throw new RuntimeException(String.format("Unable to get reference codec for %s", referencePath));
        }

        final ReferenceSequenceFile refSeqFile = fastaV1Decoder.getReferenceSequenceFile();

        return new ReferenceSource(refSeqFile);
    }
}
