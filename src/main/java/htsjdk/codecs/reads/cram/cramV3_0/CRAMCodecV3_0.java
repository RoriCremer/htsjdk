package htsjdk.codecs.reads.cram.cramV3_0;

import htsjdk.codecs.reads.cram.CRAMCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsEncoder;
import htsjdk.plugin.reads.ReadsEncoderOptions;

import java.io.InputStream;
import java.io.OutputStream;

// TODO: This should reject CRAM 3.1
/**
 * BAM codec used to exercise the reader factory infrastructure
 */
public class CRAMCodecV3_0 extends CRAMCodec {

    public static final HtsCodecVersion VERSION_3_0 = new HtsCodecVersion(3, 0, 0);

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_3_0;
    }

    @Override
    public int getSignatureSize() {
        return CRAM_MAGIC.length();
    }

    @Override
    public ReadsDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, new ReadsDecoderOptions());
    }

    @Override
    public ReadsDecoder getDecoder(final IOPath inputPath, final ReadsDecoderOptions readsDecoderOptions) {
        return new CRAMDecoderV3_0(inputPath, readsDecoderOptions);
    }

    @Override
    public ReadsDecoder getDecoder(final InputStream is, final String displayName) {
        return getDecoder(is, displayName, new ReadsDecoderOptions());
    }

    @Override
    public ReadsDecoder getDecoder(final InputStream is, final String displayName, final ReadsDecoderOptions readsDecoderOptions) {
        return new CRAMDecoderV3_0(is, displayName, readsDecoderOptions);
    }

    @Override
    public ReadsEncoder getEncoder(final IOPath outputPath) {
        return getEncoder(outputPath, new ReadsEncoderOptions());
    }

    @Override
    public ReadsEncoder getEncoder(final IOPath outputPath, final ReadsEncoderOptions readsEncoderOptions) {
        return new CRAMEncoderV3_0(outputPath, readsEncoderOptions);
    }

    @Override
    public ReadsEncoder getEncoder(final OutputStream os, final String displayName) {
        return new CRAMEncoderV3_0(os, displayName);
    }

    @Override
    public ReadsEncoder getEncoder(final OutputStream os, final String displayName, final ReadsEncoderOptions readsEncoderOptions) {
        return new CRAMEncoderV3_0(os, displayName, readsEncoderOptions);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not implemented");
    }

}
