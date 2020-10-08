package htsjdk.codecs.hapref.fasta;

import htsjdk.codecs.hapref.HapRefWriter;
import htsjdk.io.IOPath;
import htsjdk.samtools.reference.FastaReferenceWriter;

public class FASTAWriterV1_0 extends HapRefWriter {

    public FASTAWriterV1_0(final IOPath outputPath) {
        super(outputPath);
    }

    @Override
    public FastaReferenceWriter getRecordWriter(Object samHeader) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public FastaReferenceWriter getRecordWriter(Object samHeader, Object o) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void close() { throw new IllegalStateException("Not implemented"); }
}
