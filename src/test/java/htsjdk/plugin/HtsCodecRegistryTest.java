package htsjdk.plugin;

import htsjdk.HtsjdkTest;
import htsjdk.codecs.reads.bam.BAMCodec;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.hapref.HaploidReferenceReader;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMSequenceRecord;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

//TODO: make temp files

public class HtsCodecRegistryTest extends HtsjdkTest {

    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

    @Test
    public void testReadsReaderForBAM() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");

        try (final ReadsReader bamReader = HtsCodecRegistry.getReadsReader(inputPath)) {
            Assert.assertNotNull(bamReader);

            final SamReader samReader = bamReader.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = samReader.getFileHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }

    @Test
    public void testReadsWriterForBAM() {
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");
        try (final ReadsWriter readsWriter = HtsCodecRegistry.getReadsWriter(outputPath)) {
            Assert.assertNotNull(readsWriter);
        }
    }

    @Test
    public void testReadsWriterForVersion() {
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");
        try (final ReadsWriter readsWriter = HtsCodecRegistry.getReadsWriter(
                outputPath,
                ReadsFormat.BAM,
                BAMCodec.BAM_DEFAULT_VERSION)) {
            Assert.assertNotNull(readsWriter);
        }
    }

        @Test
    public void testHapRefReader() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "/hg19mini.fasta");

        try (final HaploidReferenceReader hapRefReader = HtsCodecRegistry.getReferenceReader(inputPath)) {
            Assert.assertNotNull(hapRefReader);

            final ReferenceSequenceFile referenceReader = hapRefReader.getRecordReader();
            Assert.assertNotNull(referenceReader);

            final List<SAMSequenceRecord> sequences = referenceReader.getSequenceDictionary().getSequences();
            Assert.assertFalse(sequences.isEmpty());
        }
    }

}
