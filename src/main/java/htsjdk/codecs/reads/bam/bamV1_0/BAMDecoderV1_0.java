package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMDecoder;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.samtools.BAMFileReader;
import htsjdk.samtools.DefaultSAMRecordFactory;
import htsjdk.samtools.PrimitiveSamReaderToSamReaderAdapter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.util.RuntimeIOException;
import htsjdk.samtools.util.zip.InflaterFactory;
import htsjdk.utils.ValidationUtils;

import java.io.IOException;
import java.io.InputStream;

public class BAMDecoderV1_0 extends BAMDecoder {

    private final SamReader samReader;
    private final SAMFileHeader samFileHeader;

    public BAMDecoderV1_0(final IOPath inputPath) {
        this(inputPath, new ReadsDecoderOptions());
    }

    public BAMDecoderV1_0(final IOPath inputPath, final ReadsDecoderOptions decoderOptions) {
        super(inputPath);
        ValidationUtils.nonNull(decoderOptions);
        samReader = getSamReader(decoderOptions);
        samFileHeader = samReader.getFileHeader();
    }

    public BAMDecoderV1_0(final InputStream is, final String displayName) {
        this(is, displayName, new ReadsDecoderOptions());
    }

    public BAMDecoderV1_0(final InputStream is, final String displayName, final ReadsDecoderOptions decoderOptions) {
        super(is, displayName);
        ValidationUtils.nonNull(decoderOptions);
        samReader = getSamReader(decoderOptions);
        samFileHeader = samReader.getFileHeader();
    }

    @Override
    public HtsCodecVersion getVersion() {
        return BAMCodecV1_0.VERSION_1;
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
            throw new HtsjdkIOException(String.format("Exception closing input stream %s for", inputPath), e);
        }
    }

    private SamReader getSamReader(final ReadsDecoderOptions decoderOptions) {
        SamReader reader;
        if (is != null) {
            //TODO: SamReaderFactory doesn't expose getters for all options (currently most are not exposed),
            // so this is currently not fully honoring the SAMFileWriterFactory

            //TODO: this BAMFileReader stream constructor required changing the member access to protected...
            try {
                final BAMFileReader bamReader = new BAMFileReader(is,
                        null,
                        false,
                        false,
                        decoderOptions.getSamReaderFactory().validationStringency(),
                        new DefaultSAMRecordFactory(),
                        new InflaterFactory());
                return new PrimitiveSamReaderToSamReaderAdapter(bamReader, SamInputResource.of(is));
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        } else {
            reader = decoderOptions.getSamReaderFactory().open(SamInputResource.of(inputPath.toPath()));
        }
        return reader;
    }
}
