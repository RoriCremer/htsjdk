package htsjdk.codecs.reads.bam;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsReader;

import java.io.InputStream;

public abstract class BAMReader implements ReadsReader {

    private final String displayName;

    protected IOPath inputPath;
    protected InputStream is;

    public BAMReader(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    public BAMReader(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }

    @Override
    final public String getDisplayName() { return displayName; }

}
