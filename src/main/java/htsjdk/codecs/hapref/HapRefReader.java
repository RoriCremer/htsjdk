package htsjdk.codecs.hapref;

import htsjdk.io.IOPath;
import htsjdk.plugin.hapref.HaploidReferenceReader;

public abstract class HapRefReader implements HaploidReferenceReader {
    protected final IOPath inputPath;

    private final String displayName;

    public HapRefReader(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    @Override
    public String getDisplayName() { return displayName; }

}
