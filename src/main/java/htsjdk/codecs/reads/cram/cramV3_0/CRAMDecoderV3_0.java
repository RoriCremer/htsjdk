package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMCodec;
import htsjdk.codecs.reads.cram.CRAMDecoder;
import htsjdk.codecs.reads.cram.CRAMDecoderOptions;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.samtools.CRAMFileReader;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.cram.ref.ReferenceSource;
import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.samtools.util.RuntimeIOException;

import java.io.IOException;
import java.io.InputStream;

// TODO: This should reject CRAM 3.1
public class CRAMDecoderV3_0 extends CRAMDecoder {
    private final ReadsDecoderOptions readsDecoderOptions;
    private final CRAMDecoderOptions cramDecoderOptions;
    private final CRAMFileReader cramReader;
    private final SAMFileHeader samFileHeader;

    public CRAMDecoderV3_0(final IOPath inputPath) {
        this(inputPath, new ReadsDecoderOptions());
    }

    public CRAMDecoderV3_0(final IOPath inputPath, final ReadsDecoderOptions readsDecoderOptions) {
        super(inputPath);
        this.readsDecoderOptions = readsDecoderOptions;
        this.cramDecoderOptions = readsDecoderOptions instanceof CRAMDecoderOptions ?
                (CRAMDecoderOptions) readsDecoderOptions :
                null;
        cramReader = getCRAMReader();
        samFileHeader = cramReader.getFileHeader();
    }

    public CRAMDecoderV3_0(final InputStream is, final String displayName) {
        this(is, displayName, new ReadsDecoderOptions());
    }

    public CRAMDecoderV3_0(final InputStream is, final String displayName, final ReadsDecoderOptions readsDecoderOptions) {
        super(is, displayName);
        this.readsDecoderOptions = readsDecoderOptions;
        this.cramDecoderOptions = readsDecoderOptions instanceof CRAMDecoderOptions ?
                (CRAMDecoderOptions) readsDecoderOptions :
                null;
        cramReader = getCRAMReader();
        samFileHeader = cramReader.getFileHeader();
    }

    @Override
    public HtsCodecVersion getVersion() {
        return CRAMCodecV3_0.VERSION_3_0;
    }

    @Override
    public SamReader getRecordReader() {
        //TODO: this resets state, but should be idempotent
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

    private CRAMFileReader getCRAMReader() {
        try {
            return new CRAMFileReader(
                    is == null ?
                        inputPath.getInputStream() :
                        is,
                    (SeekableStream) null,
                    readsDecoderOptions.getReferencePath() == null ?
                            ReferenceSource.getDefaultCRAMReferenceSource() :
                            CRAMCodec.getCRAMReferenceSource(readsDecoderOptions.getReferencePath()),
                    readsDecoderOptions.getSamReaderFactory().validationStringency());
        } catch (IOException e) {
            throw new RuntimeIOException(String.format("Failure opening reader for %s", getDisplayName()));
        }
    }

}
