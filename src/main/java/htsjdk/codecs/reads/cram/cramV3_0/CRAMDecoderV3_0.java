package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMDecoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
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

// TODO: This should reject CRAM 3.1
public class CRAMDecoderV3_0 extends CRAMDecoder {
    private final CRAMFileReader cramReader;
    private final SAMFileHeader samFileHeader;

    public CRAMDecoderV3_0(final IOPath inputPath) {
        this(inputPath, SamReaderFactory.makeDefault());
    }

    public CRAMDecoderV3_0(final IOPath inputPath, final SamReaderFactory samReaderFactory) {
        super(inputPath);
        try {
            cramReader = new CRAMFileReader(
                    inputPath.getInputStream(),
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
            throw new RuntimeIOException(String.format("Failure opening reader for %s", inputPath));
        }

        samFileHeader = cramReader.getFileHeader();
    }

    public CRAMDecoderV3_0(InputStream is, String displayName) {
        this(is, displayName, SamReaderFactory.makeDefault());
    }

    public CRAMDecoderV3_0(InputStream is, String displayName, final SamReaderFactory samReaderFactory) {
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
    public HtsCodecVersion getVersion() {
        return CRAMCodecV3_0.VERSION_3_0;
    }

    @Override
    public SamReader getRecordReader() {
        return new SamReader.PrimitiveSamReaderToSamReaderAdapter(cramReader, SamInputResource.of(inputPath.toPath()));
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
