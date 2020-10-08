package htsjdk.codecs.reads.cram;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsWriter;

import java.io.OutputStream;

public abstract class CRAMWriter implements ReadsWriter {
    // TODO: presorted
    protected IOPath outputPath;
    protected OutputStream os;
    final protected String displayName;


    public CRAMWriter(final IOPath outputPath) {
        this.outputPath = outputPath;
        this.displayName = outputPath.getRawInputString();
    }

    public CRAMWriter(final OutputStream os, final String displayName) {
        this.os = os;
        this.displayName = displayName;
    }

    @Override
    final public String getDisplayName() { return displayName; }

}
