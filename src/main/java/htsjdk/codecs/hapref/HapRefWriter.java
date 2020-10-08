package htsjdk.codecs.hapref;

import htsjdk.io.IOPath;
import htsjdk.plugin.hapref.HaploidReferenceWriter;

public abstract class HapRefWriter implements HaploidReferenceWriter {
    protected final IOPath outputPath;

    private final String displayName;

    public HapRefWriter(final IOPath outputPath) {
        this.outputPath = outputPath;
        this.displayName = outputPath.getRawInputString();
    }

    @Override
    public String getDisplayName() { return displayName; }
}
