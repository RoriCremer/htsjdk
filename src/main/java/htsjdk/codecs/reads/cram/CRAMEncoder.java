package htsjdk.codecs.reads.cram;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsEncoder;

import java.io.OutputStream;

/**
 * Base class for CRAM encoders.
 */
public abstract class CRAMEncoder implements ReadsEncoder {
    // TODO: presorted
    protected IOPath outputPath;
    protected OutputStream os;
    final protected String displayName;

    public CRAMEncoder(final IOPath outputPath) {
        this.outputPath = outputPath;
        this.displayName = outputPath.getRawInputString();
    }

    public CRAMEncoder(final OutputStream os, final String displayName) {
        this.os = os;
        this.displayName = displayName;
    }

    @Override
    final public ReadsFormat getFormat() { return ReadsFormat.CRAM; }

    @Override
    final public String getDisplayName() { return displayName; }

}
