package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMWriter;
import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsWriter;
import htsjdk.samtools.BAMFileWriter;
import htsjdk.samtools.CRAMFileWriter;
import htsjdk.samtools.Defaults;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMFileWriterFactory;
import htsjdk.samtools.SAMSequenceRecord;
import htsjdk.samtools.cram.ref.CRAMReferenceSource;

import java.io.OutputStream;

public class CRAMWriterV3_0 extends CRAMWriter {

    private CRAMFileWriter cramFileWriter;

    public CRAMWriterV3_0(final IOPath outputPath) {
        super(outputPath);
    }

    public CRAMWriterV3_0(final OutputStream os, final String displayName) {
        super(os, displayName);
    }

    @Override
    public SAMFileWriter getRecordWriter(final SAMFileHeader samFileHeader) {
        //TODO: fix this reference!
        cramFileWriter = new CRAMFileWriter(outputPath.getOutputStream(), new CRAMReferenceSource() {
            @Override
            public byte[] getReferenceBases(SAMSequenceRecord sequenceRecord, boolean tryNameVariants) {
                return new byte[0];
            }
        }, samFileHeader, outputPath.toString());
        cramFileWriter.setHeader(samFileHeader);
        return cramFileWriter;
    }

    @Override
    public SAMFileWriter getRecordWriter(SAMFileHeader samHeader, SAMFileWriterFactory samFileWriterFactory) {
        return null;
    }

    @Override
    public void close() {
        if (cramFileWriter != null) {
            cramFileWriter.close();
        }
    }

}
