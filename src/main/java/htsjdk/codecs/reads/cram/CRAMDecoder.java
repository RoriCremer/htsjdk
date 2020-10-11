package htsjdk.codecs.reads.cram;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsDecoder;

import java.io.InputStream;

public abstract class CRAMDecoder implements ReadsDecoder {
    private final String displayName;
    protected IOPath inputPath;
    protected InputStream is;

    public CRAMDecoder(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    public CRAMDecoder(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }

    @Override
    final public ReadsFormat getFormat() { return ReadsFormat.CRAM; }

    @Override
    final public String getDisplayName() { return displayName; }

}
