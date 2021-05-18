package htsjdk.beta.plugin.registry;

import htsjdk.HtsjdkTest;
import htsjdk.beta.codecs.reads.bam.BAMCodec;
import htsjdk.beta.plugin.bundle.Bundle;
import htsjdk.beta.plugin.bundle.BundleBuilder;
import htsjdk.beta.plugin.bundle.BundleResourceType;
import htsjdk.beta.plugin.bundle.IOPathResource;
import htsjdk.beta.plugin.bundle.InputStreamResource;
import htsjdk.beta.plugin.reads.ReadsBundle;
import htsjdk.beta.plugin.reads.ReadsDecoder;
import htsjdk.beta.plugin.reads.ReadsDecoderOptions;
import htsjdk.beta.plugin.reads.ReadsEncoder;
import htsjdk.beta.plugin.reads.ReadsFormat;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.ValidationStringency;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HtsReadsCodecTest extends HtsjdkTest {
    final IOPath TEST_DIR = new HtsPath("src/test/resources/htsjdk/samtools/");

    @DataProvider(name="readsCodecTestCases")
    public Object[][] getCodecInputResolutionSucceeds() {
        return new Object[][]{
                { BundleBuilder.start().addPrimary(
                        new IOPathResource(
                                new HtsPath(TEST_DIR + "example.bam"),
                                BundleResourceType.READS)).getBundle() },

                { BundleBuilder.start().addPrimary(
                        new InputStreamResource(
                                new HtsPath(TEST_DIR + "example.bam").getInputStream(),
                                "testReadsStream",
                                BundleResourceType.READS)).getBundle() },
        };
    }

    @Test(dataProvider = "readsCodecTestCases")
    public void testReadsDecoderForBundle(final Bundle readsBundle) {
        try (final ReadsDecoder readsDecoder = HtsReadsCodecs.getReadsDecoder(readsBundle)) {
            Assert.assertNotNull(readsDecoder);
            Assert.assertEquals(readsDecoder.getFormat(), ReadsFormat.BAM);
            Assert.assertEquals(readsDecoder.getVersion(), BAMCodec.BAM_DEFAULT_VERSION);

            final SAMFileHeader samFileHeader = readsDecoder.getHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }

    @Test
    public void testGetReadsDecoderForIOPath() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");

        try (final ReadsDecoder readsDecoder = HtsReadsCodecs.getReadsDecoder(inputPath)) {
            Assert.assertNotNull(readsDecoder);
            Assert.assertEquals(readsDecoder.getFormat(), ReadsFormat.BAM);
            Assert.assertEquals(readsDecoder.getVersion(), BAMCodec.BAM_DEFAULT_VERSION);

            final SAMFileHeader samFileHeader = readsDecoder.getHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }

    @Test
    public void testReadsDecoderForBAMWithOptions() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");

        final ReadsDecoderOptions readsDecoderOptions = new ReadsDecoderOptions();
        readsDecoderOptions.getSamReaderFactory().validationStringency(ValidationStringency.SILENT);

        try (final ReadsDecoder readsDecoder = HtsReadsCodecs.getReadsDecoder(inputPath, readsDecoderOptions)) {
            Assert.assertNotNull(readsDecoder);
            Assert.assertEquals(readsDecoder.getFormat(), ReadsFormat.BAM);
            Assert.assertEquals(readsDecoder.getVersion(), BAMCodec.BAM_DEFAULT_VERSION);

            //TODO: assert validation stringency

            final SAMFileHeader samFileHeader = readsDecoder.getHeader();
            Assert.assertNotNull(samFileHeader);

            Assert.assertEquals(samFileHeader.getSortOrder(), SAMFileHeader.SortOrder.coordinate);
        }
    }

    @Test
    public void testReadsEncoderForBAM() {
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");

        try (final ReadsEncoder readsEncoder = HtsReadsCodecs.getReadsEncoder(outputPath)) {
            Assert.assertNotNull(readsEncoder);
            Assert.assertEquals(readsEncoder.getFormat(), ReadsFormat.BAM);
            Assert.assertEquals(readsEncoder.getVersion(), BAMCodec.BAM_DEFAULT_VERSION);

            readsEncoder.setHeader(new SAMFileHeader());
        }
    }

    @Test
    public void testReadsEncoderForVersion() {
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");

        try (final ReadsEncoder readsEncoder = HtsReadsCodecs.getReadsEncoder(
                outputPath,
                ReadsFormat.BAM,
                BAMCodec.BAM_DEFAULT_VERSION)) {
            Assert.assertNotNull(readsEncoder);
            Assert.assertEquals(readsEncoder.getFormat(), ReadsFormat.BAM);
            Assert.assertEquals(readsEncoder.getVersion(), BAMCodec.BAM_DEFAULT_VERSION);
        }
    }

    @Test
    public void testRoundTripReads() {
        final IOPath inputPath = new HtsPath(TEST_DIR + "example.bam");
        final IOPath outputPath = new HtsPath("pluginTestOutput.bam");

        try (final ReadsDecoder readDecoder = HtsReadsCodecs.getReadsDecoder(inputPath);
             final ReadsEncoder readsEncoder = HtsReadsCodecs.getReadsEncoder(outputPath)) {

            Assert.assertNotNull(readDecoder);
            Assert.assertEquals(readDecoder.getFormat(), ReadsFormat.BAM);
            Assert.assertNotNull(readsEncoder);
            Assert.assertEquals(readsEncoder.getFormat(), ReadsFormat.BAM);

            final SAMFileHeader samFileHeader = readDecoder.getHeader();
            Assert.assertNotNull(samFileHeader);

            readsEncoder.setHeader(samFileHeader);
            for (final SAMRecord samRec : readDecoder) {
                readsEncoder.write(samRec);
            }
        }
    }

}
