package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMEncoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.samtools.CRAMFileWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMFileWriterFactory;
import htsjdk.samtools.SAMSequenceRecord;
import htsjdk.samtools.cram.ref.CRAMReferenceSource;

import java.io.OutputStream;

// TODO: This should reject CRAM 3.1
public class CRAMEncoderV3_0 extends CRAMEncoder {

    private CRAMFileWriter cramFileWriter;

    public CRAMEncoderV3_0(final IOPath outputPath) {
        this(outputPath, new SAMFileWriterFactory());
    }

    public CRAMEncoderV3_0(final IOPath outputPath, final SAMFileWriterFactory samFileWriterFactory) {
        super(outputPath);
    }

    public CRAMEncoderV3_0(final OutputStream os, final String displayName) {
        super(os, displayName);
    }

    public CRAMEncoderV3_0(final OutputStream os, final String displayName, final SAMFileWriterFactory samFileWriterFactory) {
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
    public HtsCodecVersion getVersion() {
        return CRAMCodecV3_0.VERSION_3_0;
    }

    @Override
    public void close() {
        if (cramFileWriter != null) {
            cramFileWriter.close();
        }
    }

}
