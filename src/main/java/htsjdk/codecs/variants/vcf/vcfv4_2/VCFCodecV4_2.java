package htsjdk.codecs.variants.vcf.vcfv4_2;

import htsjdk.codecs.variants.vcf.VCFCodec;
import htsjdk.codecs.variants.vcf.VCFDecoder;
import htsjdk.codecs.variants.vcf.VCFEncoder;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.bundle.InputBundle;
import htsjdk.plugin.variants.VariantsDecoderOptions;
import htsjdk.plugin.variants.VariantsEncoderOptions;
import htsjdk.samtools.util.IOUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public class VCFCodecV4_2 extends VCFCodec {
    public static HtsCodecVersion VCF_V42_VERSION = new HtsCodecVersion(4, 2,0);
    protected static final String VCF_V42_MAGIC = "##fileformat=VCFv4.2";

    @Override
    public HtsCodecVersion getVersion() { return VCF_V42_VERSION; }

    @Override
    public int getSignatureSize() {
        return VCF_V42_MAGIC.length();
    }

    @Override
    public boolean canDecodeSignature(final InputStream rawInputStream, final String sourceName) {
        final byte[] signatureBytes = new byte[getSignatureSize()];
        try (final BufferedInputStream bis = new BufferedInputStream(rawInputStream);
             final InputStream wrappedInputStream = IOUtil.isGZIPInputStream(bis) ? new GZIPInputStream(bis) : bis) {
                final int numRead = wrappedInputStream.read(signatureBytes);
                if (numRead <= 0) {
                    throw new IOException(String.format("0 bytes read from input stream for %s", sourceName));
                }
        } catch (IOException e) {
            throw new HtsjdkIOException(String.format("Failure reading signature from stream for %s", sourceName), e);
        }
        return Arrays.equals(VCF_V42_MAGIC.getBytes(), signatureBytes);
    }

    @Override
    public VCFDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, new VariantsDecoderOptions());
    }

    @Override
    public VCFDecoder getDecoder(final IOPath inputPath, final VariantsDecoderOptions decoderOptions) {
        return new VCFDecoderV4_2(inputPath, decoderOptions);
    }

    @Override
    public VCFDecoder getDecoder(final InputBundle inputBundle, final VariantsDecoderOptions decoderOptions) {
        return new VCFDecoderV4_2(inputBundle, decoderOptions);
    }

    @Override
    public VCFDecoder getDecoder(final InputStream is, final String displayName) {
        return getDecoder(is, displayName, new VariantsDecoderOptions());
    }

    @Override
    public VCFDecoder getDecoder(final InputStream is, final String displayName, final VariantsDecoderOptions decoderOptions) {
        return new VCFDecoderV4_2(is, displayName, decoderOptions);
    }

    @Override
    public VCFEncoder getEncoder(final IOPath outputPath) {
        return new VCFEncoderV4_2(outputPath);
    }

    @Override
    public VCFEncoder getEncoder(final IOPath outputPath, final VariantsEncoderOptions encoderOptions) {
        return new VCFEncoderV4_2(outputPath, encoderOptions);
    }

    @Override
    public VCFEncoder getEncoder(final OutputStream os, final String displayName) {
        return new VCFEncoderV4_2(os, displayName);
    }

    @Override
    public VCFEncoder getEncoder(final OutputStream os, final String displayName, final VariantsEncoderOptions encoderOptions) {
        return new VCFEncoderV4_2(os, displayName, encoderOptions);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new RuntimeException("Not yet implemented");
    }

}
