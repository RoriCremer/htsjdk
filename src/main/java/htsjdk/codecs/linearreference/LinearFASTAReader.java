package htsjdk.codecs.linearreference;

import htsjdk.plugin.linearreference.LinearReferenceReader;

import java.io.InputStream;

public abstract class LinearFASTAReader implements LinearReferenceReader {
    final protected InputStream is;
    final protected String displayName;

    public LinearFASTAReader(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }
}
