package htsjdk.codecs;

import htsjdk.HtsjdkTest;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecRegistry;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SamReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HtsCRAMCodecTest extends HtsjdkTest {
    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

    @Test
    public void testCRAMDecoder() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "cram/ce#unmap2.3.0.cram");

        try (final ReadsDecoder cramDecoder = HtsCodecRegistry.getReadsDecoder(inputPath)) {
            Assert.assertNotNull(cramDecoder);
            Assert.assertEquals(cramDecoder.getFormat(), ReadsFormat.CRAM);

            final SamReader samReader = cramDecoder.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = samReader.getFileHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.unsorted);
        }
    }

}
