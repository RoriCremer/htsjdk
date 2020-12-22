package htsjdk.codecs.reads.htsget;

import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.bundle.BundleResourceType;
import htsjdk.plugin.bundle.InputBundle;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsFormat;

import java.io.InputStream;

public abstract class HtsgetBAMDecoder implements ReadsDecoder {
    protected IOPath inputPath;
    protected InputStream is;
    protected InputBundle inputBundle;

    private final String displayName;

    public HtsgetBAMDecoder(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    public HtsgetBAMDecoder(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }

    public HtsgetBAMDecoder(final InputBundle inputBundle, final ReadsDecoderOptions decoderOptions) {
        this.inputBundle = inputBundle;
        //TODO: handle optional.empty()...
        this.displayName = inputBundle.get(BundleResourceType.READS).get().getIOPath().get().getRawInputString();
    }

    @Override
    final public ReadsFormat getFormat() { return ReadsFormat.BAM; }

    @Override
    public HtsCodecVersion getVersion() {
        return HtsgetBAMCodec.HTSGET_VERSION;
    }

    @Override
    final public String getDisplayName() { return displayName; }

}
