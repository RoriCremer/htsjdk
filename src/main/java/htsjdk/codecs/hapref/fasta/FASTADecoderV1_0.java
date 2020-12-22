package htsjdk.codecs.hapref.fasta;

import htsjdk.codecs.hapref.HapRefDecoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.bundle.InputBundle;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.samtools.reference.ReferenceSequence;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import htsjdk.samtools.reference.ReferenceSequenceFileFactory;
import htsjdk.samtools.util.RuntimeIOException;

import java.io.IOException;
import java.util.Iterator;

/**
 * A FASTA file decoder.
 */
public class FASTADecoderV1_0 extends HapRefDecoder {

    private ReferenceSequenceFile referenceSequenceFile;

    public FASTADecoderV1_0(final IOPath inputPath) {
        super(inputPath);
        referenceSequenceFile = ReferenceSequenceFileFactory.getReferenceSequenceFile(inputPath.toPath());
    }

    public FASTADecoderV1_0(final InputBundle inputBundle) {
        super(inputBundle);
        referenceSequenceFile = ReferenceSequenceFileFactory.getReferenceSequenceFile(inputPath.toPath());
    }

    @Override
    final public HaploidReferenceFormat getFormat() { return HaploidReferenceFormat.FASTA; }

    @Override
    public SAMSequenceDictionary getHeader() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HtsCodecVersion getVersion() {
        return FASTACodecV1_0.VERSION_1;
    }

    //TODO: this needs to consult the inputBundle
    @Override
    public Iterator<ReferenceSequence> iterator() {
        referenceSequenceFile.reset();
        return new Iterator<ReferenceSequence>() {
            ReferenceSequence nextSeq = referenceSequenceFile.nextSequence();

            @Override
            public boolean hasNext() {
                return nextSeq != null;
            }

            @Override
            public ReferenceSequence next() {
                final ReferenceSequence tmpSeq = nextSeq;
                nextSeq = referenceSequenceFile.nextSequence();
                return tmpSeq;
            }
        };
    }

    //TODO: this shouldn't be necessary
    public ReferenceSequenceFile getReferenceSequenceFile() {
        return referenceSequenceFile;
    }

    @Override
    public void close() {
        if (referenceSequenceFile != null) {
            try {
                referenceSequenceFile.close();
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        }
    }

}
