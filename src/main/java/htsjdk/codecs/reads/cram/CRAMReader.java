package htsjdk.codecs.reads.cram;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsReader;

import java.io.InputStream;

public abstract class CRAMReader implements ReadsReader {

    private final String displayName;

    protected IOPath inputPath;
    protected InputStream is;

    public CRAMReader(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    public CRAMReader(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }

    @Override
    final public String getDisplayName() { return displayName; }

}
