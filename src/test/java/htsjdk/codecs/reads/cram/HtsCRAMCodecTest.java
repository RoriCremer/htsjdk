package htsjdk.codecs.reads.cram;

import htsjdk.HtsjdkTest;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecRegistry;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsEncoder;
import htsjdk.plugin.reads.ReadsEncoderOptions;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMRecord;
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

    @Test
    public void testRoundTripCRAM() {
        final IOPath cramInputPath = new HtsPath(TEST_DIR + "cram/c2#pad.3.0.cram");
        final IOPath cramOutputPath = new HtsPath("pluginTestOutput.cram");
        final IOPath referencePath = new HtsPath(TEST_DIR + "cram/c2.fa");

        final ReadsDecoderOptions readsDecoderOptions = new ReadsDecoderOptions().setReferencePath(referencePath);
        final ReadsEncoderOptions readsEncoderOptions = new ReadsEncoderOptions().setReferencePath(referencePath);

        try (final ReadsDecoder cramDecoder = HtsCodecRegistry.getReadsDecoder(cramInputPath, readsDecoderOptions);
             final ReadsEncoder cramEncoder = HtsCodecRegistry.getReadsEncoder(cramOutputPath, readsEncoderOptions)) {

            Assert.assertNotNull(cramDecoder);
            Assert.assertEquals(cramDecoder.getFormat(), ReadsFormat.CRAM);
            Assert.assertNotNull(cramEncoder);
            Assert.assertEquals(cramEncoder.getFormat(), ReadsFormat.CRAM);

            final SamReader samReader = cramDecoder.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = cramDecoder.getHeader();
            Assert.assertNotNull(samFileHeader);

            final SAMFileWriter samFileWriter = cramEncoder.getRecordWriter(samFileHeader);
            for (final SAMRecord samRec : samReader) {
                samFileWriter.addAlignment(samRec);
            }
        }
    }

}
