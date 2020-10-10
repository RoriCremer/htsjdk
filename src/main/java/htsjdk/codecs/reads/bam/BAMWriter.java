package htsjdk.codecs.reads.bam;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsWriter;

import java.io.OutputStream;

public abstract class BAMWriter implements ReadsWriter {

    // TODO: presorted

    final private String displayName;
    protected IOPath outputPath;
    protected OutputStream os;

    public BAMWriter(final IOPath outputPath) {
        this.outputPath = outputPath;
        this.displayName = outputPath.getRawInputString();
    }

    public BAMWriter(final OutputStream os, final String displayName) {
        this.os = os;
        this.displayName = displayName;
    }

    @Override
    final public ReadsFormat getFormat() { return ReadsFormat.BAM; }

    @Override
    final public String getDisplayName() { return displayName; }

}
