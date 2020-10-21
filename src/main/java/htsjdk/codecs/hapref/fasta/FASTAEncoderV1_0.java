package htsjdk.codecs.hapref.fasta;

import htsjdk.codecs.hapref.HapRefEncoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.UnusedType;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.samtools.reference.FastaReferenceWriter;

public class FASTAEncoderV1_0 extends HapRefEncoder {

    public FASTAEncoderV1_0(final IOPath outputPath) {
        super(outputPath);
    }

    @Override
    final public HaploidReferenceFormat getFormat() { return HaploidReferenceFormat.FASTA; }

    @Override
    public FastaReferenceWriter getRecordWriter(final UnusedType unused) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HtsCodecVersion getVersion() {
        return FASTACodecV1_0.VERSION_1;
    }

    @Override
    public void close() { throw new IllegalStateException("Not implemented"); }
}