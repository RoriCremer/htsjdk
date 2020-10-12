package htsjdk.codecs.hapref.fasta;

import htsjdk.codecs.hapref.HapRefCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.UnusedType;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.plugin.hapref.HaploidReferenceDecoder;
import htsjdk.plugin.hapref.HaploidReferenceEncoder;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class FASTACodecV1_0 extends HapRefCodec {

    public static final HtsCodecVersion VERSION_1 = new HtsCodecVersion(1, 0, 0);

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_1;
    }

    @Override
    public HaploidReferenceFormat getFormat() {
        return HaploidReferenceFormat.FASTA;
    }

    @Override
    public int getSignatureSize() {
        return 1;
    }

    @Override
    public boolean canDecodeExtension(final Path path) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public boolean canDecodeSignature(final byte[] streamSignature) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HaploidReferenceDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, null);
    }

    @Override
    public HaploidReferenceDecoder getDecoder(final IOPath inputPath, final UnusedType options) {
        ValidationUtils.validateArg(options == null, "reference reader options must be null");
        return new FASTADecoderV1_0(inputPath);
    }

    @Override
    public HaploidReferenceDecoder getDecoder(final InputStream is, final String displayName) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HaploidReferenceDecoder getDecoder(InputStream is, String displayName, UnusedType unusedType) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HaploidReferenceEncoder getEncoder(final IOPath outputPath) {
        return getEncoder(outputPath, null);
    }

    @Override
    public HaploidReferenceEncoder getEncoder(final IOPath outputPath, final UnusedType options) {
        ValidationUtils.nonNull(options, "reference reader options must be null");
        return new FASTAEncoderV1_0(outputPath);
    }

    @Override
    public HaploidReferenceEncoder getEncoder(final OutputStream os, final String displayName) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HaploidReferenceEncoder getEncoder(final OutputStream os, final String displayName, final UnusedType unusedType) {
        return null;
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not implemented");
    }
}
