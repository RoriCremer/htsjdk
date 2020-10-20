package htsjdk.codecs.reads.cram;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.samtools.SamReaderFactory;

public class CRAMDecoderOptions extends ReadsDecoderOptions {
    private SamReaderFactory samReaderFactory = SamReaderFactory.makeDefault();
    private IOPath referencePath;

    public SamReaderFactory getSamReaderFactory() {
        return samReaderFactory;
    }

    public CRAMDecoderOptions setSamReaderFactory(final SamReaderFactory samReaderFactory) {
        this.samReaderFactory = samReaderFactory;
        return this;
    }

    public IOPath getReferencePath() {
        return referencePath;
    }

    public CRAMDecoderOptions setReferencePath(final IOPath referencePath) {
        this.referencePath = referencePath;
        return this;
    }

}
