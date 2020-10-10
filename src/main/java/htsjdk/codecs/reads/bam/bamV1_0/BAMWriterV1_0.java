package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMWriter;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.samtools.BAMFileWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMFileWriterFactory;
import htsjdk.samtools.util.BlockCompressedOutputStream;

import java.io.OutputStream;

//TODO: a writer needs to write several streams (file, index, md5), not one
// TODO: should this take a REFERENCE_READER from a codec ? from options
//TODO: this needs a header to create the

public class BAMWriterV1_0 extends BAMWriter {

    private final SAMFileWriterFactory samFileWriterFactory;
    private SAMFileWriter samFileWriter;

    public BAMWriterV1_0(final IOPath outputPath) {
        super(outputPath);
        this.samFileWriterFactory = new SAMFileWriterFactory();;
    }

    public BAMWriterV1_0(final IOPath outputPath, final SAMFileWriterFactory samFileWriterFactory) {
        super(outputPath);
        this.samFileWriterFactory = samFileWriterFactory;
    }

    public BAMWriterV1_0(final OutputStream os, final String displayName) {
        super(os, displayName);
        this.samFileWriterFactory = new SAMFileWriterFactory();
    }

    public BAMWriterV1_0(final OutputStream os, final String displayName, final SAMFileWriterFactory samFileWriterFactory) {
        super(os, displayName);
        this.samFileWriterFactory = samFileWriterFactory;
    }

    @Override
    public HtsCodecVersion getVersion() {
        return BAMCodecV1_0.VERSION_1;
    }

    @Override
    public SAMFileWriter getRecordWriter(final SAMFileHeader samFileHeader) {
        samFileWriter = getBAMFileWriter(new SAMFileWriterFactory(), samFileHeader);
        return samFileWriter;
    }

    @Override
    public void close() {
        if (samFileWriter != null) {
            samFileWriter.close();
        }
    }

    private SAMFileWriter getBAMFileWriter(final SAMFileWriterFactory samFileWriterFactory, final SAMFileHeader samFileHeader) {
        //TODO: expose presorted
        final boolean preSorted = true;

        if (os != null) {
            //TODO: SAMFileWriterFactory doesn't expose getters for all options (currently most are not exposed),
            // so this is currently not fully honoring the SAMFileWriterFactory

            //TODO: this stream constructor required changing the member access to protected...
            final BAMFileWriter bamFileWriter = new BAMFileWriter(
                    os,
                    getDisplayName(),
                    samFileWriterFactory.getCompressionLevel(),
                    BlockCompressedOutputStream.getDefaultDeflaterFactory());
            bamFileWriter.setHeader(samFileHeader);
            return bamFileWriter;
        } else {
            return samFileWriterFactory.makeBAMWriter(samFileHeader, preSorted, outputPath.toPath());
        }
    }
}
