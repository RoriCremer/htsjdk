package htsjdk.codecs.hapref.fasta;

import htsjdk.codecs.hapref.HapRefCodec;
import htsjdk.codecs.hapref.HapRefDecoder;
import htsjdk.codecs.hapref.HapRefEncoder;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsDecoderOptions;
import htsjdk.plugin.HtsEncoderOptions;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.hapref.HaploidReferenceBundle;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.samtools.util.FileExtensions;
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
    public HaploidReferenceFormat getFileFormat() {
        return HaploidReferenceFormat.FASTA;
    }

    @Override
    public int getSignatureSize() {
        return 1;
    }

    @Override
    public boolean canDecodeSignature(final InputStream rawInputStream, final String sourceName) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public boolean canDecodeExtension(final IOPath ioPath) {
        return FileExtensions.FASTA.stream().anyMatch(ext-> ioPath.hasExtension(ext));
    }

    @Override
    public boolean canDecodeExtension(final Path path) {
        return FileExtensions.FASTA.stream().anyMatch(ext-> path.endsWith(ext));
    }

    @Override
    public HapRefDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, null);
    }

    @Override
    public HapRefDecoder getDecoder(final HaploidReferenceBundle inputBundle, final HtsDecoderOptions options) {
        ValidationUtils.validateArg(options == null, "reference reader options must be null");
        return new FASTADecoderV1_0(inputBundle);
    }

    @Override
    public HapRefDecoder getDecoder(final IOPath inputPath, final HtsDecoderOptions options) {
        ValidationUtils.validateArg(options == null, "reference reader options must be null");
        return new FASTADecoderV1_0(inputPath);
    }

    @Override
    public HapRefDecoder getDecoder(final InputStream is, final String displayName) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HapRefDecoder getDecoder(InputStream is, String displayName, HtsDecoderOptions emptyType) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HapRefEncoder getEncoder(final IOPath outputPath) {
        return getEncoder(outputPath, null);
    }

    @Override
    public HapRefEncoder getEncoder(final IOPath outputPath, final HtsEncoderOptions options) {
        ValidationUtils.nonNull(options, "reference reader options must be null");
        return new FASTAEncoderV1_0(outputPath);
    }

    @Override
    public HapRefEncoder getEncoder(final OutputStream os, final String displayName) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HapRefEncoder getEncoder(final OutputStream os, final String displayName, final HtsEncoderOptions emptyType) {
        return null;
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not implemented");
    }
}
