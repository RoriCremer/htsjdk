package htsjdk.codecs.reads.cram;

import htsjdk.io.IOPath;
import htsjdk.plugin.bundle.BundleResourceType;
import htsjdk.plugin.bundle.InputBundle;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsDecoder;

import java.io.InputStream;

/**
 * Base class for CRAM decoders.
 */
public abstract class CRAMDecoder implements ReadsDecoder {
    protected InputBundle inputBundle;
    protected IOPath inputPath;
    protected InputStream is;

    protected final ReadsDecoderOptions readsDecoderOptions;

    private final String displayName;

    public CRAMDecoder(final IOPath inputPath, final ReadsDecoderOptions readsDecoderOptions) {
        this.inputPath = inputPath;
        this.readsDecoderOptions = readsDecoderOptions;
        this.displayName = inputPath.getRawInputString();
    }

    public CRAMDecoder(final InputStream is, final String displayName, final ReadsDecoderOptions readsDecoderOptions) {
        this.is = is;
        this.readsDecoderOptions = readsDecoderOptions;
        this.displayName = displayName;
    }

    public CRAMDecoder(final InputBundle inputBundle, final ReadsDecoderOptions readsDecoderOptions) {
        this.inputBundle = inputBundle;
        this.readsDecoderOptions = readsDecoderOptions;
        this.displayName = inputBundle.get(BundleResourceType.READS).get().getDisplayName();
    }

    @Override
    final public ReadsFormat getFormat() { return ReadsFormat.CRAM; }

    @Override
    final public String getDisplayName() { return displayName; }

}
