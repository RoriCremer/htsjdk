package htsjdk.beta.codecs.hapref;

import htsjdk.io.IOPath;
import htsjdk.beta.plugin.bundle.InputBundle;
import htsjdk.beta.plugin.hapref.HaploidReferenceDecoder;

/**
 * Base class for haploid reference decoders.
 */
public abstract class HapRefDecoder implements HaploidReferenceDecoder {
    protected IOPath inputPath;
    protected InputBundle haprefBundle;
    private final String displayName;

    public HapRefDecoder(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    public HapRefDecoder(final InputBundle inputBundle) {
        this.haprefBundle = inputBundle;
        //TODO: fix this
        this.displayName = "TEMP";
    }

    @Override
    public String getDisplayName() { return displayName; }

}
