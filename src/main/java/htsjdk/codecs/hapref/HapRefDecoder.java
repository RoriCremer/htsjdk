package htsjdk.codecs.hapref;

import htsjdk.io.IOPath;
import htsjdk.plugin.hapref.HaploidReferenceDecoder;

public abstract class HapRefDecoder implements HaploidReferenceDecoder {
    protected final IOPath inputPath;

    private final String displayName;

    public HapRefDecoder(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    @Override
    public String getDisplayName() { return displayName; }

}
