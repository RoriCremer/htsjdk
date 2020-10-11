package htsjdk.codecs.reads.bam;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsDecoder;

import java.io.InputStream;

public abstract class BAMDecoder implements ReadsDecoder {

    private final String displayName;

    protected IOPath inputPath;
    protected InputStream is;

    public BAMDecoder(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    public BAMDecoder(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }

    @Override
    final public ReadsFormat getFormat() { return ReadsFormat.BAM; }

    @Override
    final public String getDisplayName() { return displayName; }

}
