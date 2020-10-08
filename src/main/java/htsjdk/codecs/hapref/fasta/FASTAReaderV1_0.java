package htsjdk.codecs.hapref.fasta;

import htsjdk.codecs.hapref.HapRefReader;
import htsjdk.io.IOPath;
import htsjdk.samtools.reference.FastaSequenceFile;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import htsjdk.samtools.util.RuntimeIOException;

import java.io.IOException;

public class FASTAReaderV1_0 extends HapRefReader {

    private ReferenceSequenceFile referenceSequenceFile;

    public FASTAReaderV1_0(final IOPath inputPath) {
        super(inputPath);
    }

    @Override
    public ReferenceSequenceFile getRecordReader() {
        // TODO: truncateNamesAtWhitespace
        referenceSequenceFile = new FastaSequenceFile(inputPath.toPath(), true);
        return referenceSequenceFile;
    }

    @Override
    public ReferenceSequenceFile getRecordReader(final Object options) {
        return new FastaSequenceFile(inputPath.toPath(), true);
    }

    @Override
    public Object getHeader() {
        throw new IllegalStateException("Not implemented");
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
