package htsjdk.codecs.reads.cram;

import htsjdk.plugin.reads.ReadsWriter;

import java.io.OutputStream;

public abstract class CRAMWriter implements ReadsWriter {
    // TODO: reference ?
    // TODO: presorted

    final protected OutputStream os;
    final protected String displayName;

    public CRAMWriter(final OutputStream os, final String displayName) {
        this.os = os;
        this.displayName = displayName;
    }
}
