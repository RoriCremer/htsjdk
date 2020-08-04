package htsjdk.codecs.bam.bamV1;

import htsjdk.exception.HtsjdkIOException;
import htsjdk.codecs.bam.BAMReader;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

import java.io.IOException;
import java.io.InputStream;

public class BAMV1Reader extends BAMReader {

    private final SamReader samReader;
    private final SAMFileHeader samFileHeader;

    public BAMV1Reader(InputStream is, String displayName) {
        super(is, displayName);
        samReader = SamReaderFactory.makeDefault().open(SamInputResource.of(is));
        samFileHeader = samReader.getFileHeader();
    }

    @Override
    public SamReader getRecordReader() {
        return samReader;
    }

    @Override
    public SAMFileHeader getHeader() {
        return samFileHeader;
    }

    @Override
    public void close() {
        try {
            samReader.close();
        } catch (IOException e) {
            throw new HtsjdkIOException(String.format("Exception closing input stream %s", displayName), e);
        }
    }
}
