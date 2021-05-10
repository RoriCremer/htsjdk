package htsjdk.beta.codecs.reads.bam;

import htsjdk.beta.plugin.bundle.Bundle;
import htsjdk.io.IOPath;
import htsjdk.beta.plugin.bundle.BundleResourceType;
import htsjdk.beta.plugin.reads.ReadsDecoderOptions;
import htsjdk.beta.plugin.reads.ReadsFormat;
import htsjdk.beta.plugin.reads.ReadsDecoder;

import java.io.InputStream;

/**
 * Base class for BAM decoders.
 */
public abstract class BAMDecoder implements ReadsDecoder {
    protected Bundle inputBundle;
    protected IOPath inputPath;
    protected InputStream is;

    protected final ReadsDecoderOptions readsDecoderOptions;

    private final String displayName;

    public BAMDecoder(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
        this.readsDecoderOptions = new ReadsDecoderOptions();
    }

    public BAMDecoder(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
        this.readsDecoderOptions = new ReadsDecoderOptions();
    }

    public BAMDecoder(final Bundle inputBundle, final ReadsDecoderOptions readsDecoderOptions) {
        this.inputBundle = inputBundle;
        this.displayName = inputBundle.get(BundleResourceType.READS).get().getDisplayName();
        this.readsDecoderOptions = readsDecoderOptions;
    }

    @Override
    final public ReadsFormat getFormat() { return ReadsFormat.BAM; }

    @Override
    final public String getDisplayName() { return displayName; }

}
