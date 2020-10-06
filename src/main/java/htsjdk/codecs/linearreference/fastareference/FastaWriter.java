package htsjdk.codecs.linearreference.fastareference;

import htsjdk.codecs.linearreference.LinearFASTAWriter;
import htsjdk.samtools.reference.FastaReferenceWriter;

import java.io.OutputStream;

public class FastaWriter extends LinearFASTAWriter {

    final protected OutputStream os;
    final protected String displayName;

    public FastaWriter(final OutputStream os, final String displayName) {
        this.os = os;
        this.displayName = displayName;
    }

    @Override
    public FastaReferenceWriter getRecordWriter(Object samHeader) {
        return null;
    }

    @Override
    public FastaReferenceWriter getRecordWriter(Object samHeader, Object o) {
        return null;
    }

    @Override
    public void close() {

    }
}
