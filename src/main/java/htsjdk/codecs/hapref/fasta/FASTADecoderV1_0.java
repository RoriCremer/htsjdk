package htsjdk.codecs.hapref.fasta;

import htsjdk.codecs.hapref.HapRefDecoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.UnusedType;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.samtools.reference.FastaSequenceFile;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import htsjdk.samtools.util.RuntimeIOException;

import java.io.IOException;

public class FASTADecoderV1_0 extends HapRefDecoder {

    private ReferenceSequenceFile referenceSequenceFile;

    public FASTADecoderV1_0(final IOPath inputPath) {
        super(inputPath);
    }

    @Override
    final public HaploidReferenceFormat getFormat() { return HaploidReferenceFormat.FASTA; }

    @Override
    public ReferenceSequenceFile getRecordReader() {
        // TODO: truncateNamesAtWhitespace
        referenceSequenceFile = new FastaSequenceFile(inputPath.toPath(), true);
        return referenceSequenceFile;
    }

    @Override
    public UnusedType getHeader() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HtsCodecVersion getVersion() {
        return FASTACodecV1_0.VERSION_1;
    }

    @Override
    public void close() {
        if (referenceSequenceFile != null) {
            try {
                referenceSequenceFile.close();
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        }
    }
}
