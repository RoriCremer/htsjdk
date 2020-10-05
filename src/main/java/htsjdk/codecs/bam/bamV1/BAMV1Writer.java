package htsjdk.codecs.bam.bamV1;

import htsjdk.codecs.bam.BAMWriter;
import htsjdk.samtools.BAMFileWriter;
import htsjdk.samtools.Defaults;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.util.BlockCompressedOutputStream;

import java.io.OutputStream;

//TODO: a writer needs to write several streams (file, index, md5), not one
// TODO: should this be a REFERENCE_READER from a codec ?

class BAMV1Writer extends BAMWriter {

    BAMFileWriter bamFileWriter;

    public BAMV1Writer(final OutputStream os, final String displayName) {
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
