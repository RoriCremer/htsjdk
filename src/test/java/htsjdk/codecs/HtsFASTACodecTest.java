package htsjdk.codecs;

import htsjdk.HtsjdkTest;
import htsjdk.codecs.hapref.HapRefReader;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecRegistry;
import htsjdk.samtools.SAMSequenceRecord;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class HtsFASTACodecTest extends HtsjdkTest {
    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

    @Test
    public void testFASTAReader() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "/hg19mini.fasta");

        try (final HapRefReader fastaReader = HtsCodecRegistry.getReferenceReader(inputPath)) {
            Assert.assertNotNull(fastaReader);

            final ReferenceSequenceFile referenceReader = fastaReader.getRecordReader();
            Assert.assertNotNull(referenceReader);

            final List<SAMSequenceRecord> sequences = referenceReader.getSequenceDictionary().getSequences();
            Assert.assertFalse(sequences.isEmpty());
        }
    }

}
