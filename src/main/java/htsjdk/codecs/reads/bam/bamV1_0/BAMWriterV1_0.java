package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMWriter;
import htsjdk.samtools.BAMFileWriter;
import htsjdk.samtools.Defaults;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.util.BlockCompressedOutputStream;

import java.io.OutputStream;

//TODO: a writer needs to write several streams (file, index, md5), not one
// TODO: should this be a REFERENCE_READER from a codec ?

class BAMWriterV1_0 extends BAMWriter {

    BAMFileWriter bamFileWriter;

    public BAMWriterV1_0(final OutputStream os, final String displayName) {
        super(os, displayName);
    }

    @Override
    public SAMFileWriter getRecordWriter(final SAMFileHeader samFileHeader) {
        final boolean preSorted = true;
        //TODO: required changing access to protected...
        bamFileWriter = new BAMFileWriter(os, displayName, Defaults.COMPRESSION_LEVEL, BlockCompressedOutputStream.getDefaultDeflaterFactory());
        bamFileWriter.setHeader(samFileHeader);
        return bamFileWriter;
    }

    @Override
    public void close() {
        if (bamFileWriter != null) {
            bamFileWriter.close();
        }
    }
}
