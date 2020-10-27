package htsjdk.plugin;

import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.plugin.hapref.HaploidReferenceCodec;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.plugin.hapref.HaploidReferenceDecoder;

import htsjdk.plugin.reads.ReadsCodec;
import htsjdk.plugin.reads.ReadsDecoder;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsEncoder;
import htsjdk.plugin.reads.ReadsEncoderOptions;
import htsjdk.plugin.reads.ReadsFormat;

import htsjdk.plugin.variants.VariantsCodec;
import htsjdk.plugin.variants.VariantsDecoder;
import htsjdk.plugin.variants.VariantsHtsDecoderOptions;
import htsjdk.plugin.variants.VariantsEncoder;
import htsjdk.plugin.variants.VariantsHtsEncoderOptions;
import htsjdk.plugin.variants.VariantsFormat;

import htsjdk.utils.ValidationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Registry/cache for discovered codecs.
 */
@SuppressWarnings("rawtypes")
public class HtsCodecRegistry {
    private static final HtsCodecRegistry htsCodecRegistry = new HtsCodecRegistry();

    // registered codecs by format
    private static HtsCodecsByType<HaploidReferenceFormat, HaploidReferenceCodec> haprefCodecs = new HtsCodecsByType<>();
    private static HtsCodecsByType<ReadsFormat, ReadsCodec> readsCodecs = new HtsCodecsByType<>();
    private static HtsCodecsByType<VariantsFormat, VariantsCodec> variantCodecs = new HtsCodecsByType<>();

    private final static String NO_CODEC_MSG_FORMAT_STRING = "A %s codec capable of handling \"%s\" could not be found";

    static {
        ServiceLoader.load(HtsCodec.class).forEach(htsCodecRegistry::registerCodec);
    }

    private HtsCodecRegistry() {}

    /**
     * Add a codec to the registry
     */
    private void registerCodec(final HtsCodec codec) {
        switch (codec.getType()) {
            case ALIGNED_READS:
                readsCodecs.register(((ReadsCodec) codec).getFileFormat(), (ReadsCodec) codec);
                break;

            case REFERENCE:
                haprefCodecs.register(((HaploidReferenceCodec) codec).getFileFormat(), (HaploidReferenceCodec) codec);
                break;

            case VARIANTS:
                variantCodecs.register(((VariantsCodec) codec).getFileFormat(), (VariantsCodec) codec);
                break;

            case FEATURES:
                throw new IllegalArgumentException("Codec type not yet implemented");

            default:
                throw new IllegalArgumentException("Unknown codec type");
        }
    }

    // **** Reads ******/

    @SuppressWarnings("unchecked")
    public static ReadsDecoder getReadsDecoder(final IOPath inputPath) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");

        final List<ReadsCodec> codecs = readsCodecs.getCodecsForIOPath(inputPath);
        final ReadsDecoder decoder = codecs
                .stream()
                .filter(codec -> canDecodeSignature(codec, inputPath))
                .map(codec -> (ReadsDecoder) codec.getDecoder(inputPath))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath)));
        return decoder;
    }

    @SuppressWarnings("unchecked")
    public static ReadsDecoder getReadsDecoder(
            final IOPath inputPath,
            final ReadsDecoderOptions readsDecoderOptions) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        ValidationUtils.nonNull(readsDecoderOptions, "Decoder options must not be null");
        final List<ReadsCodec> codecs = readsCodecs.getCodecsForIOPath(inputPath);
        final ReadsDecoder decoder = codecs
                .stream()
                .filter(codec -> canDecodeSignature(codec, inputPath))
                .map(codec -> (ReadsDecoder) codec.getDecoder(inputPath, readsDecoderOptions))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath)));
        return decoder;
    }

    @SuppressWarnings("unchecked")
    public static ReadsEncoder getReadsEncoder(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        return (ReadsEncoder) readsCodecs.getCodecForIOPath(outputPath)
                .map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", outputPath)));
    }

    @SuppressWarnings("unchecked")
    public static ReadsEncoder getReadsEncoder(
            final IOPath outputPath,
            final ReadsEncoderOptions readsEncoderOptions) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(readsEncoderOptions, "Encoder options must not be null");
        return (ReadsEncoder) readsCodecs.getCodecForIOPath(outputPath)
                .map(codec -> codec.getEncoder(outputPath, readsEncoderOptions))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", outputPath)));
    }

    @SuppressWarnings("unchecked")
    public static ReadsEncoder getReadsEncoder(
            final IOPath outputPath,
            final ReadsFormat readsFormat,
            final HtsCodecVersion codecVersion) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(readsFormat, "Codec format must not be null");
        ValidationUtils.nonNull(codecVersion, "Codec version must not be null");
        return (ReadsEncoder) (readsCodecs.getCodecForFormatAndVersion(readsFormat, codecVersion)
                .map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", outputPath))));
    }

    // **** Variants ******/

    @SuppressWarnings("unchecked")
    public static VariantsDecoder getVariantsDecoder(final IOPath inputPath) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        final List<VariantsCodec> codecs = variantCodecs.getCodecsForIOPath(inputPath);
        final VariantsDecoder decoder = codecs
                .stream()
                .filter(codec -> canDecodeSignature(codec, inputPath))
                .map(codec -> (VariantsDecoder) codec.getDecoder(inputPath))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath)));
        return decoder;
    }

    @SuppressWarnings("unchecked")
    public static VariantsDecoder getVariantsDecoder(
            final IOPath inputPath,
            final VariantsHtsDecoderOptions variantsDecoderOptions) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        ValidationUtils.nonNull(variantsDecoderOptions, "Decoder options must not be null");
        final List<VariantsCodec> codecs = variantCodecs.getCodecsForIOPath(inputPath);
        final VariantsDecoder decoder = codecs
                .stream()
                .filter(codec -> canDecodeSignature(codec, inputPath))
                .map(codec -> (VariantsDecoder) codec.getDecoder(inputPath, variantsDecoderOptions))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath)));
        return decoder;
    }

    @SuppressWarnings("unchecked")
    public static VariantsEncoder getVariantsEncoder(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForIOPath(outputPath);
        return (VariantsEncoder) (variantCodec.map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static VariantsEncoder getVariantsEncoder(
            final IOPath outputPath,
            final VariantsHtsEncoderOptions variantsEncoderOptions) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(variantsEncoderOptions, "Encoder options must not be null");
        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForIOPath(outputPath);
        return (VariantsEncoder) (variantCodec.map(codec -> codec.getEncoder(outputPath, variantsEncoderOptions))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static VariantsEncoder getVariantsEncoder(
            final IOPath outputPath,
            final VariantsFormat variantsFormat,
            final HtsCodecVersion codecVersion) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(variantsFormat, "Format must not be null");
        ValidationUtils.nonNull(codecVersion, "Codec version must not be null");
        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForFormatAndVersion(variantsFormat, codecVersion);
        return (VariantsEncoder) (variantCodec.map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

    //TODO: validate stream signature
    @SuppressWarnings("unchecked")
    public static HaploidReferenceDecoder getHapRefDecoder(final IOPath inputPath) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        final Optional<HaploidReferenceCodec> haploidReferenceCodec = haprefCodecs.getCodecForIOPath(inputPath);
        return (HaploidReferenceDecoder) (haploidReferenceCodec.map(codec -> codec.getDecoder(inputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "hapref", inputPath))));
    }

    @SuppressWarnings("rawtypes")
    private static<T extends HtsCodec> boolean canDecodeSignature(final T codec, final IOPath inputPath) {
        try (final InputStream rawInputStream = inputPath.getInputStream()) {
            return codec.canDecodeSignature(rawInputStream, inputPath.getRawInputString());
        } catch (IOException e) {
            throw new HtsjdkIOException(String.format("Failure reading signature from stream for %s", inputPath.getRawInputString()), e);
        }
    }

}

