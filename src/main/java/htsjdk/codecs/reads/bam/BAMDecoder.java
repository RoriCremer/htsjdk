package htsjdk.codecs.reads.bam;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsBundle;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsDecoder;

import java.io.InputStream;

public abstract class BAMDecoder implements ReadsDecoder {

    private final String displayName;

    protected IOPath inputPath;
    protected InputStream is;
    protected ReadsBundle inputBundle;

    public BAMDecoder(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    public BAMDecoder(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }

    public BAMDecoder(final ReadsBundle inputBundle, final ReadsDecoderOptions decoderOptions) {
        this.inputBundle = inputBundle;
        this.displayName = inputBundle.getReads().getRawInputString();
    }

    @Override
    final public ReadsFormat getFormat() { return ReadsFormat.BAM; }

    @Override
    final public String getDisplayName() { return displayName; }

}
