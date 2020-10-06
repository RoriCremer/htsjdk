package htsjdk.codecs.reads.cram;

import htsjdk.plugin.reads.ReadsReader;

import java.io.InputStream;

public abstract class CRAMReader implements ReadsReader {

    final protected InputStream is;
    final protected String displayName;

    public CRAMReader(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }
}
