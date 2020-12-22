package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMEncoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.HtsEncoderOptions;
import htsjdk.plugin.reads.ReadsEncoderOptions;
import htsjdk.samtools.BAMFileWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.util.BlockCompressedOutputStream;

import java.io.OutputStream;

public class BAMEncoderV1_0 extends BAMEncoder {

    private final HtsEncoderOptions readsEncoderOptions;
    private SAMFileWriter samFileWriter;

    public BAMEncoderV1_0(final IOPath outputPath) {
        super(outputPath);
        this.readsEncoderOptions = new ReadsEncoderOptions();
    }

    public BAMEncoderV1_0(final IOPath outputPath, final HtsEncoderOptions readsEncoderOptions) {
        super(outputPath);
        this.readsEncoderOptions = readsEncoderOptions;
    }

    public BAMEncoderV1_0(final OutputStream os, final String displayName) {
        super(os, displayName);
        this.readsEncoderOptions = new ReadsEncoderOptions();
    }

    public BAMEncoderV1_0(final OutputStream os, final String displayName, final HtsEncoderOptions readsEncoderOptions) {
        super(os, displayName);
        this.readsEncoderOptions = readsEncoderOptions;
    }

    @Override
    public HtsCodecVersion getVersion() {
        return BAMCodecV1_0.VERSION_1;
    }

    @Override
    public void setHeader(final SAMFileHeader samFileHeader) {
        samFileWriter = getBAMFileWriter(readsEncoderOptions, samFileHeader);
    }

    @Override
    public void write(final SAMRecord record) {
        samFileWriter.addAlignment(record);
    }

    @Override
    public void close() {
        if (samFileWriter != null) {
            samFileWriter.close();
        }
    }

    private SAMFileWriter getBAMFileWriter(final HtsEncoderOptions htsEncoderOptions, final SAMFileHeader samFileHeader) {
        //TODO: expose presorted
        final boolean preSorted = true;

        final ReadsEncoderOptions readsEncoderOptions = (ReadsEncoderOptions) htsEncoderOptions;

        if (os != null) {
            //TODO: SAMFileWriterFactory doesn't expose getters for all options (currently most are not exposed),
            // so this is currently not fully honoring the SAMFileWriterFactory

            //TODO: this stream constructor required changing the member access to protected...
            final BAMFileWriter bamFileWriter = new BAMFileWriter(
                    os,
                    getDisplayName(),
                    readsEncoderOptions.getSamFileWriterFactory().getCompressionLevel(),
                    BlockCompressedOutputStream.getDefaultDeflaterFactory());
            bamFileWriter.setHeader(samFileHeader);
            return bamFileWriter;
        } else {
            return readsEncoderOptions.getSamFileWriterFactory().makeBAMWriter(samFileHeader, preSorted, outputPath.toPath());
        }
    }
}
