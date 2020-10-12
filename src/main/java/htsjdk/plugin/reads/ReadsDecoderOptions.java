package htsjdk.plugin.reads;

import htsjdk.io.IOPath;
import htsjdk.samtools.SamReaderFactory;

public class ReadsDecoderOptions {
    private SamReaderFactory samReaderFactory = SamReaderFactory.makeDefault();
    private IOPath referencePath;

    public SamReaderFactory getSamReaderFactory() {
        return samReaderFactory;
    }

    public ReadsDecoderOptions setSamReaderFactory(final SamReaderFactory samReaderFactory) {
        this.samReaderFactory = samReaderFactory;
        return this;
    }

    public IOPath getReferencePath() {
        return referencePath;
    }

    public ReadsDecoderOptions setReferencePath(final IOPath referencePath) {
        this.referencePath = referencePath;
        return this;
    }
}
