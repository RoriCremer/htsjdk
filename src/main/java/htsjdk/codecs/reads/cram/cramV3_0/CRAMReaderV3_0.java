package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMReader;
import htsjdk.samtools.CRAMFileReader;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMSequenceRecord;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.ValidationStringency;
import htsjdk.samtools.cram.ref.CRAMReferenceSource;
import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.samtools.util.RuntimeIOException;

import java.io.IOException;
import java.io.InputStream;

public class CRAMReaderV3_0 extends CRAMReader {
    private final CRAMFileReader cramReader;
    private final SAMFileHeader samFileHeader;

    public CRAMReaderV3_0(InputStream is, String displayName) {
        super(is, displayName);
        try {
            cramReader = new CRAMFileReader(
                    is,
                    (SeekableStream) null,
                    //TODO: fix this reference!
                    new CRAMReferenceSource() {
                        @Override
                        public byte[] getReferenceBases(SAMSequenceRecord sequenceRecord, boolean tryNameVariants) {
                            return new byte[0];
                        }
                    },
                    ValidationStringency.DEFAULT_STRINGENCY);
        } catch (IOException e) {
            throw new RuntimeIOException(String.format("Failure opening reader for %s", displayName));
        }

        samFileHeader = cramReader.getFileHeader();
    }

    @Override
    public SamReader getRecordReader() {
        return new SamReader.PrimitiveSamReaderToSamReaderAdapter(cramReader, SamInputResource.of(is));
    }

    @Override
    public SamReader getRecordReader(SamReaderFactory samReaderFactory) {
        return null;
    }

    @Override
    public SAMFileHeader getHeader() {
        return samFileHeader;
    }

    @Override
    public void close() {
        cramReader.close();
    }
}
