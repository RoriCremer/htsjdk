package htsjdk.plugin;

import htsjdk.HtsjdkTest;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SamReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HtsCRAMCodecTest extends HtsjdkTest {
    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

    @Test
    public void testReadsReaderForCRAM() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "cram/ce#unmap2.3.0.cram");

        try (final ReadsReader cramReader = HtsCodecRegistry.getReadsReader(inputPath)) {
            Assert.assertNotNull(cramReader);

            final SamReader samReader = cramReader.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = samReader.getFileHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.unsorted);
        }
    }

}
