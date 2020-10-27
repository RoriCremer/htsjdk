package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMCodec;
import htsjdk.codecs.reads.cram.CRAMDecoder;
import htsjdk.codecs.reads.cram.CRAMDecoderOptions;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.HtsDecoderOptions;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.samtools.CRAMFileReader;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.cram.ref.ReferenceSource;
import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.samtools.util.RuntimeIOException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class CRAMDecoderV3_0 extends CRAMDecoder {
    private final HtsDecoderOptions htsDecoderOptions;
    private final CRAMDecoderOptions cramDecoderOptions;
    private final CRAMFileReader cramReader;
    private final SAMFileHeader samFileHeader;

    public CRAMDecoderV3_0(final IOPath inputPath) {
        this(inputPath, new ReadsDecoderOptions());
    }

    public CRAMDecoderV3_0(final IOPath inputPath, final HtsDecoderOptions readsDecoderOptions) {
        super(inputPath);
        this.htsDecoderOptions = readsDecoderOptions;
        this.cramDecoderOptions = readsDecoderOptions instanceof CRAMDecoderOptions ?
                (CRAMDecoderOptions) readsDecoderOptions :
                null;
        cramReader = getCRAMReader();
        samFileHeader = cramReader.getFileHeader();
    }

    public CRAMDecoderV3_0(final InputStream is, final String displayName) {
        this(is, displayName, new ReadsDecoderOptions());
    }

    public CRAMDecoderV3_0(final InputStream is, final String displayName, final HtsDecoderOptions readsDecoderOptions) {
        super(is, displayName);
        this.htsDecoderOptions = readsDecoderOptions;
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
    public SAMFileHeader getHeader() {
        return samFileHeader;
    }

    @Override
    public Iterator<SAMRecord> iterator() {
        return cramReader.getIterator();
    }

    @Override
    public void close() {
        cramReader.close();
    }

    private CRAMFileReader getCRAMReader() {
        final ReadsDecoderOptions readsDecoderOptions = (ReadsDecoderOptions) htsDecoderOptions;
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
