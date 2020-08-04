package htsjdk.codecs.bam;

import htsjdk.plugin.reads.ReadsWriter;

import java.io.OutputStream;

public abstract class BAMWriter implements ReadsWriter {

    // TODO: reference ?
    // TODO: presorted

    final protected OutputStream os;
    final protected String displayName;

    public BAMWriter(final OutputStream os, final String displayName) {
        this.os = os;
        this.displayName = displayName;
    }

}
