package htsjdk.plugin;

import htsjdk.HtsjdkTest;
import htsjdk.codecs.hapref.fasta.FASTACodecV1_0;
import htsjdk.codecs.reads.bam.BAMCodec;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.plugin.hapref.HaploidReferenceDecoder;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsEncoder;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMSequenceRecord;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.ValidationStringency;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

//TODO: make temp files

public class HtsCodecRegistryTest extends HtsjdkTest {

    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

    @Test
    public void testReadsDecoderForBAM() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");

        try (final ReadsDecoder readsDecoder = HtsCodecRegistry.getReadsDecoder(inputPath)) {
            Assert.assertNotNull(readsDecoder);
            Assert.assertEquals(readsDecoder.getFormat(), ReadsFormat.BAM);
            Assert.assertEquals(readsDecoder.getVersion(), BAMCodec.BAM_DEFAULT_VERSION);

            final SamReader samReader = readsDecoder.getRecordReader();
            Assert.assertNotNull(samReader);

            final SAMFileHeader samFileHeader = samReader.getFileHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }

    @Test
    public void testReadsDecoderForBAMWithOptions() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");

        final ReadsDecoderOptions readsDecoderOptions = new ReadsDecoderOptions();
        readsDecoderOptions.getSamReaderFactory().validationStringency(ValidationStringency.SILENT);
        try (final ReadsDecoder readsDecoder = HtsCodecRegistry.getReadsDecoder(inputPath, readsDecoderOptions)) {
            Assert.assertNotNull(readsDecoder);
            Assert.assertEquals(readsDecoder.getFormat(), ReadsFormat.BAM);
            Assert.assertEquals(readsDecoder.getVersion(), BAMCodec.BAM_DEFAULT_VERSION);

            final SamReader samReader = readsDecoder.getRecordReader();
            Assert.assertNotNull(samReader);
            //TODO: assert validation stringency

            final SAMFileHeader samFileHeader = samReader.getFileHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }
    @Test
    public void testReadsEncoderForBAM() {
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");

        try (final ReadsEncoder readsEncoder = HtsCodecRegistry.getReadsEncoder(outputPath)) {
            Assert.assertNotNull(readsEncoder);
            Assert.assertEquals(readsEncoder.getFormat(), ReadsFormat.BAM);
            Assert.assertEquals(readsEncoder.getVersion(), BAMCodec.BAM_DEFAULT_VERSION);

            final SAMFileWriter samFileWriter = readsEncoder.getRecordWriter(new SAMFileHeader());
            Assert.assertNotNull(samFileWriter);
        }
    }

    @Test
    public void testReadsEncoderForVersion() {
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");

        try (final ReadsEncoder readsEncoder = HtsCodecRegistry.getReadsEncoder(
                outputPath,
                ReadsFormat.BAM,
                BAMCodec.BAM_DEFAULT_VERSION)) {
            Assert.assertNotNull(readsEncoder);
            Assert.assertEquals(readsEncoder.getFormat(), ReadsFormat.BAM);
            Assert.assertEquals(readsEncoder.getVersion(), BAMCodec.BAM_DEFAULT_VERSION);
        }
    }

    @Test
    public void testHapRefDecoder() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "/hg19mini.fasta");

        try (final HaploidReferenceDecoder hapRefDecoder = HtsCodecRegistry.getHapRefDecoder(inputPath)) {
            Assert.assertNotNull(hapRefDecoder);
            Assert.assertEquals(hapRefDecoder.getFormat(), HaploidReferenceFormat.FASTA);
            Assert.assertEquals(hapRefDecoder.getVersion(), FASTACodecV1_0.VERSION_1);

            final ReferenceSequenceFile referenceReader = hapRefDecoder.getRecordReader();
            Assert.assertNotNull(referenceReader);

            final List<SAMSequenceRecord> sequences = referenceReader.getSequenceDictionary().getSequences();
            Assert.assertFalse(sequences.isEmpty());
        }
    }

}
