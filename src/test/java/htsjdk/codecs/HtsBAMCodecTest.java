package htsjdk.codecs;

import htsjdk.HtsjdkTest;
import htsjdk.codecs.reads.bam.BAMReader;
import htsjdk.codecs.reads.bam.BAMWriter;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecRegistry;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HtsBAMCodecTest  extends HtsjdkTest {
    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

    @Test
    public void testBAMReaderForBAM() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");

        try (final BAMReader bamReader = HtsCodecRegistry.getReadsReader(inputPath)) {
            Assert.assertNotNull(bamReader);

            final SamReader samReader = bamReader.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = samReader.getFileHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }

    @Test
    public void testBAMWriterForBAM() {
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");
        try (final BAMWriter bamWriter = HtsCodecRegistry.getReadsWriter(outputPath)) {
            Assert.assertNotNull(bamWriter);
        }
    }

    @Test
    public void testReadWriteRoundTripForBAM() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");

        try (final ReadsReader bamReader = HtsCodecRegistry.getReadsReader(inputPath);
             final ReadsWriter bamWriter = HtsCodecRegistry.getReadsWriter(outputPath)) {
            Assert.assertNotNull(bamReader);
            Assert.assertNotNull(bamWriter);

            final SamReader samReader = bamReader.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = bamReader.getHeader();
            Assert.assertNotNull(samFileHeader);

            final SAMFileWriter samFileWriter = bamWriter.getRecordWriter(samFileHeader);
            for (final SAMRecord samRec : samReader) {
                samFileWriter.addAlignment(samRec);
            }
        }
    }

}
