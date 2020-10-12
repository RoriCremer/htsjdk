package htsjdk.plugin.reads;

import htsjdk.io.IOPath;
import htsjdk.samtools.SAMFileWriterFactory;
import htsjdk.samtools.cram.ref.ReferenceSource;

public class ReadsEncoderOptions {
    private SAMFileWriterFactory samFileWriterFactory = new SAMFileWriterFactory();
    private IOPath referencePath;

    public SAMFileWriterFactory getSamFileWriterFactory() {
        return samFileWriterFactory;
    }

    public ReadsEncoderOptions setSamFileWriterFactory(final SAMFileWriterFactory samFileWriterFactory) {
        this.samFileWriterFactory = samFileWriterFactory;
        return this;
    }

    public IOPath getReferencePath() {
        return referencePath;
    }

    public ReadsEncoderOptions setReferencePath(final IOPath referencePath) {
        this.referencePath = referencePath;
        return this;
    }
}
