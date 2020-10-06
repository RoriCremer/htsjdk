package htsjdk.codecs.linearreference.fastareference;

import htsjdk.codecs.linearreference.LinearFASTAReader;
import htsjdk.samtools.reference.ReferenceSequenceFile;

import java.io.InputStream;

public class FastaReader extends LinearFASTAReader {

    public FastaReader(InputStream is, String displayName) {
        super(is, displayName);
    }

    @Override
    public ReferenceSequenceFile getRecordReader() {
        return null;
    }

    @Override
    public ReferenceSequenceFile getRecordReader(final Object options) {
        return null;
    }

    @Override
    public Object getHeader() {
        throw new RuntimeException("getHeader not implemented for fasta reader");
    }

    @Override
    public void close() {

    }
}
