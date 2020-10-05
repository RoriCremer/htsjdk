package htsjdk.codecs.reads.bam;

import htsjdk.plugin.reads.ReadsReader;

import java.io.InputStream;

public abstract class BAMReader implements ReadsReader {

    final protected InputStream is;
    final protected String displayName;

    public BAMReader(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }

}
