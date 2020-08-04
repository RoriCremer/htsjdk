package htsjdk.plugin;

import htsjdk.HtsjdkTest;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.codecs.bam.BAMReader;
import htsjdk.codecs.bam.BAMWriter;
import htsjdk.plugin.reads.ReadsWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamReader;
import org.testng.Assert;
import org.testng.annotations.Test;

//TODO: make temp files

public class HtsCodecRegistryTest extends HtsjdkTest {

    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

   @Test
    public void testReadsReader() {
        final IOPath ioPath = new HtsPath(TEST_DIR + "example.bam");

        try (final BAMReader bamReader = HtsCodecRegistry.getReadsReader(ioPath)) {
            Assert.assertNotNull(bamReader);

            final SamReader samReader = bamReader.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = samReader.getFileHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }

    @Test
    public void testReadsWriterForPath() {
        final IOPath outputPath = new HtsPath("jomama.bam");
        try (final ReadsWriter readsWriter = HtsCodecRegistry.getReadsWriter(outputPath)) {
            Assert.assertNotNull(readsWriter);
        }
    }

    @Test
    public void testReadsWriterForVersion() {
        final IOPath outputPath = new HtsPath("jomama.bam");
        try (final BAMWriter readsWriter = HtsCodecRegistry.getReadsWriter(
                outputPath,
                ReadsFormat.BAM,
                HtsCodecRegistry.BAM_DEFAULT_VERSION)) {
            Assert.assertNotNull(readsWriter);
        }
    }

    @Test
    public void testReadsRoundTrip() {
        final IOPath ioPath = new HtsPath(TEST_DIR + "example.bam");
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");

        //TODO: where does an upgrade happen ? Who bridges the version incompatibilities ?
        //TODO: if the output ioPath were a CRAM, how would the BAMWriter cast fail (ie. since it should
        // be either ReadsWriter or CRAMWriter)
        try (final BAMReader bamReader = HtsCodecRegistry.getReadsReader(ioPath);
             final BAMWriter bamWriter = HtsCodecRegistry.getReadsWriter(outputPath)) {
            Assert.assertNotNull(bamReader);
            Assert.assertNotNull(bamWriter);

            final SamReader samReader = bamReader.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = bamReader.getHeader();
            Assert.assertNotNull(samFileHeader);

            final SAMFileWriter samFileWriter = bamWriter.getRecordWriter(samFileHeader);
            for (SAMRecord samRec : samReader) {
                samFileWriter.addAlignment(samRec);
            }
        }
    }

}
