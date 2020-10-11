package htsjdk.codecs;

import htsjdk.HtsjdkTest;
import htsjdk.codecs.reads.bam.BAMDecoder;
import htsjdk.codecs.reads.bam.BAMEncoder;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecRegistry;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsEncoder;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HtsBAMCodecTest  extends HtsjdkTest {
    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

    @Test
    public void testBAMDecoder() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");

        try (final BAMDecoder bamDecoder = HtsCodecRegistry.getReadsDecoder(inputPath)) {
            Assert.assertNotNull(bamDecoder);
            Assert.assertEquals(bamDecoder.getFormat(), ReadsFormat.BAM);

            final SamReader samReader = bamDecoder.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = samReader.getFileHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }

    @Test
    public void testBAMEncoder() {
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");
        try (final BAMEncoder bamEncoder = HtsCodecRegistry.getReadsEncoder(outputPath)) {
            Assert.assertNotNull(bamEncoder);
            Assert.assertEquals(bamEncoder.getFormat(), ReadsFormat.BAM);
        }
    }

    @Test
    public void testRoundTripBAM() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");

        try (final ReadsDecoder bamDecoder = HtsCodecRegistry.getReadsDecoder(inputPath);
             final ReadsEncoder bamEncoder = HtsCodecRegistry.getReadsEncoder(outputPath)) {

            Assert.assertNotNull(bamDecoder);
            Assert.assertEquals(bamDecoder.getFormat(), ReadsFormat.BAM);
            Assert.assertNotNull(bamEncoder);
            Assert.assertEquals(bamEncoder.getFormat(), ReadsFormat.BAM);

            final SamReader samReader = bamDecoder.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = bamDecoder.getHeader();
            Assert.assertNotNull(samFileHeader);

            final SAMFileWriter samFileWriter = bamEncoder.getRecordWriter(samFileHeader);
            for (final SAMRecord samRec : samReader) {
                samFileWriter.addAlignment(samRec);
            }
        }
    }

}
