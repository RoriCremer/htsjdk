package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMReader;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

import java.io.IOException;
import java.io.InputStream;

class BAMReaderV1_0 extends BAMReader {

    private SamReader samReader;
    private SAMFileHeader samFileHeader;

    public BAMReaderV1_0(InputStream is, String displayName) {
        super(is, displayName);
    }

    @Override
    public SamReader getRecordReader() {
        return getSamReader(SamReaderFactory.makeDefault());
    }

    @Override
    public SamReader getRecordReader(final SamReaderFactory customSamReaderFactory) {
        return getSamReader(customSamReaderFactory);
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

    private SamReader getSamReader(final SamReaderFactory samReaderFactory) {
        samReader = samReaderFactory.open(SamInputResource.of(is));
        samFileHeader = samReader.getFileHeader();
        return samReader;
    }
}
