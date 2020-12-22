package htsjdk.codecs.reads.bam;

import htsjdk.io.IOPath;
import htsjdk.plugin.bundle.BundleResourceType;
import htsjdk.plugin.bundle.InputBundle;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsDecoder;

import java.io.InputStream;

/**
 * Base class for BAM decoders.
 */
public abstract class BAMDecoder implements ReadsDecoder {
    protected InputBundle inputBundle;
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

    public BAMDecoder(final InputBundle inputBundle, final ReadsDecoderOptions readsDecoderOptions) {
        this.inputBundle = inputBundle;
        //TODO: Handle and Optional.empty()
        this.displayName = inputBundle.get(BundleResourceType.READS).get().getIOPath().get().getRawInputString();
        this.readsDecoderOptions = readsDecoderOptions;
    }

    @Override
    final public ReadsFormat getFormat() { return ReadsFormat.BAM; }

    @Override
    final public String getDisplayName() { return displayName; }

}
