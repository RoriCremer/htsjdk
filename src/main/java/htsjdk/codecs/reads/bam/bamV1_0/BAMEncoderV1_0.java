package htsjdk.codecs.reads.bam.bamV1_0;

import htsjdk.codecs.reads.bam.BAMEncoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsEncoderOptions;
import htsjdk.samtools.BAMFileWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.util.BlockCompressedOutputStream;

import java.io.OutputStream;

//TODO: a writer needs to write several streams (file, index, md5), not one
// TODO: should this take a REFERENCE_READER from a codec ? from options
//TODO: this needs a header to create the

public class BAMEncoderV1_0 extends BAMEncoder {

    private final ReadsEncoderOptions readsEncoderOptions;
    private SAMFileWriter samFileWriter;

    public BAMEncoderV1_0(final IOPath outputPath) {
        super(outputPath);
        this.readsEncoderOptions = new ReadsEncoderOptions();
    }

    public BAMEncoderV1_0(final IOPath outputPath, final ReadsEncoderOptions readsEncoderOptions) {
        super(outputPath);
        this.readsEncoderOptions = readsEncoderOptions;
    }

    public BAMEncoderV1_0(final OutputStream os, final String displayName) {
        super(os, displayName);
        this.readsEncoderOptions = new ReadsEncoderOptions();
    }

    public BAMEncoderV1_0(final OutputStream os, final String displayName, final ReadsEncoderOptions readsEncoderOptions) {
        super(os, displayName);
        this.readsEncoderOptions = readsEncoderOptions;
    }

    @Override
    public HtsCodecVersion getVersion() {
        return BAMCodecV1_0.VERSION_1;
    }

    @Override
    public SAMFileWriter getRecordWriter(final SAMFileHeader samFileHeader) {
        samFileWriter = getBAMFileWriter(readsEncoderOptions, samFileHeader);
        return samFileWriter;
    }

    @Override
    public void close() {
        if (samFileWriter != null) {
            samFileWriter.close();
        }
    }

    private SAMFileWriter getBAMFileWriter(final ReadsEncoderOptions readsEncoderOptions, final SAMFileHeader samFileHeader) {
        //TODO: expose presorted
        final boolean preSorted = true;

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
