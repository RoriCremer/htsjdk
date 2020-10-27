package htsjdk.codecs.reads.bam;

import htsjdk.HtsjdkTest;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecRegistry;
import htsjdk.plugin.HtsHeader;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HtsBAMCodecTest  extends HtsjdkTest {
    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

    @Test
    public void testBAMDecoder() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");

        try (final BAMDecoder bamDecoder = (BAMDecoder) HtsCodecRegistry.getReadsDecoder(inputPath)) {
            Assert.assertNotNull(bamDecoder);
            Assert.assertEquals(bamDecoder.getFormat(), ReadsFormat.BAM);

            final SAMFileHeader samFileHeader = bamDecoder.getHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }

    @Test
    public void testBAMEncoder() {
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");
        try (final BAMEncoder bamEncoder = (BAMEncoder) HtsCodecRegistry.getReadsEncoder(outputPath)) {
            Assert.assertNotNull(bamEncoder);
            Assert.assertEquals(bamEncoder.getFormat(), ReadsFormat.BAM);
        }
    }

    @Test
    public void testRoundTripBAM() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");

        try (final BAMDecoder bamDecoder = (BAMDecoder) HtsCodecRegistry.getReadsDecoder(inputPath);
             final BAMEncoder bamEncoder = (BAMEncoder) HtsCodecRegistry.getReadsEncoder(outputPath)) {

            Assert.assertNotNull(bamDecoder);
            Assert.assertEquals(bamDecoder.getFormat(), ReadsFormat.BAM);
            Assert.assertNotNull(bamEncoder);
            Assert.assertEquals(bamEncoder.getFormat(), ReadsFormat.BAM);

            final SAMFileHeader samFileHeader = bamDecoder.getHeader();
            Assert.assertNotNull(samFileHeader);

            bamEncoder.setHeader(samFileHeader);
            for (final SAMRecord samRec : bamDecoder) {
                bamEncoder.write(samRec);
            }
        }
    }

}
