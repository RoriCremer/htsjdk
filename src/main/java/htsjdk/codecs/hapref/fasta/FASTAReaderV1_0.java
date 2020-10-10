package htsjdk.codecs.hapref.fasta;

import htsjdk.codecs.hapref.HapRefReader;
import htsjdk.codecs.reads.bam.bamV1_0.BAMCodecV1_0;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.UnusedType;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.samtools.reference.FastaSequenceFile;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import htsjdk.samtools.util.RuntimeIOException;

import java.io.IOException;

public class FASTAReaderV1_0 extends HapRefReader {

    private ReferenceSequenceFile referenceSequenceFile;

    public FASTAReaderV1_0(final IOPath inputPath) {
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
