package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMCodec;
import htsjdk.codecs.reads.cram.CRAMEncoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.HtsEncoderOptions;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsEncoderOptions;
import htsjdk.samtools.CRAMFileWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.cram.ref.ReferenceSource;

import java.io.OutputStream;

public class CRAMEncoderV3_0 extends CRAMEncoder {

    final private HtsEncoderOptions htsEncoderOptions;
    private CRAMFileWriter cramFileWriter;

    public CRAMEncoderV3_0(final IOPath outputPath) {
        this(outputPath, new ReadsEncoderOptions());
    }

    public CRAMEncoderV3_0(final IOPath outputPath, final HtsEncoderOptions readsEncoderOptions) {
        super(outputPath);
        this.htsEncoderOptions = readsEncoderOptions;
    }

    public CRAMEncoderV3_0(final OutputStream os, final String displayName) {
        this(os, displayName, new ReadsEncoderOptions());
    }

    public CRAMEncoderV3_0(final OutputStream os, final String displayName, final HtsEncoderOptions readsEncoderOptions) {
        super(os, displayName);
        this.htsEncoderOptions = readsEncoderOptions;
    }

    @Override
    public void setHeader(final SAMFileHeader samFileHeader) {
        cramFileWriter = getCRAMWriter(samFileHeader);
    }

    @Override
    public void write(final SAMRecord record) {
        cramFileWriter.addAlignment(record);
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

    private CRAMFileWriter getCRAMWriter(final SAMFileHeader samFileHeader) {
        final ReadsEncoderOptions readsEncoderOptions = (ReadsEncoderOptions) htsEncoderOptions;
        cramFileWriter = new CRAMFileWriter(
                outputPath.getOutputStream(),
                readsEncoderOptions.getReferencePath() == null ?
                        ReferenceSource.getDefaultCRAMReferenceSource() :
                        CRAMCodec.getCRAMReferenceSource(readsEncoderOptions.getReferencePath()),
                samFileHeader,
                outputPath.toString());
        return cramFileWriter;
    }

}
