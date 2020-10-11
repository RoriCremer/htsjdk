package htsjdk.codecs.hapref;

import htsjdk.io.IOPath;
import htsjdk.plugin.hapref.HaploidReferenceEncoder;

public abstract class HapRefEncoder implements HaploidReferenceEncoder {
    protected final IOPath outputPath;

    private final String displayName;

    public HapRefEncoder(final IOPath outputPath) {
        this.outputPath = outputPath;
        this.displayName = outputPath.getRawInputString();
    }

    @Override
    public String getDisplayName() { return displayName; }
}
