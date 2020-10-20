package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.exception.HtsjdkPluginException;
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
import htsjdk.plugin.variants.VariantsDecoderOptions;
import htsjdk.plugin.variants.VariantsEncoder;
import htsjdk.plugin.variants.VariantsEncoderOptions;
import htsjdk.plugin.variants.VariantsFormat;

import htsjdk.utils.ValidationUtils;

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

    // minimum number of bytes required to allow any codec to decide if it can decode a stream
    private static int minSignatureSize = 0;
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
                readsCodecs.register(((ReadsCodec) codec).getFormat(), (ReadsCodec) codec);
                break;

            case REFERENCE:
                haprefCodecs.register(((HaploidReferenceCodec) codec).getFormat(), (HaploidReferenceCodec) codec);
                break;

            case VARIANTS:
                variantCodecs.register(((VariantsCodec) codec).getFormat(), (VariantsCodec) codec);
                break;

            case FEATURES:
                throw new IllegalArgumentException("Codec type not yet implemented");

            default:
                throw new IllegalArgumentException("Unknown codec type");
        }

        final int minSignatureBytesRequired = codec.getSignatureSize();
        if (minSignatureBytesRequired < 1) {
            throw new HtsjdkPluginException(
                    String.format("%s: file signature size must be > 0", codec.getDisplayName())
            );
        }
        minSignatureSize = Integer.max(minSignatureSize, minSignatureBytesRequired);
    }

    // **** Reads ******/

    @SuppressWarnings("unchecked")
    public static<T extends ReadsDecoder> T getReadsDecoder(final IOPath inputPath) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        return (T) (readsCodecs.getCodecForIOPath(inputPath)
                .map(codec -> codec.getDecoder(inputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath))));
    }

    @SuppressWarnings("unchecked")
    public static<T extends ReadsDecoder> T getReadsDecoder(
            final IOPath inputPath,
            final ReadsDecoderOptions readsDecoderOptions) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        ValidationUtils.nonNull(readsDecoderOptions, "Decoder options must not be null");
        return (T) (readsCodecs.getCodecForIOPath(inputPath)
                .map(codec -> codec.getDecoder(inputPath, readsDecoderOptions))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath))));
    }

    @SuppressWarnings("unchecked")
    public static<T extends ReadsEncoder> T getReadsEncoder(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        return (T) (readsCodecs.getCodecForIOPath(outputPath)
                .map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static<T extends ReadsEncoder> T getReadsEncoder(
            final IOPath outputPath,
            final ReadsEncoderOptions readsEncoderOptions) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(readsEncoderOptions, "Encoder options must not be null");
        return (T) (readsCodecs.getCodecForIOPath(outputPath)
                .map(codec -> codec.getEncoder(outputPath, readsEncoderOptions))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static <T extends ReadsEncoder> T getReadsEncoder(
            final IOPath outputPath,
            final ReadsFormat readsFormat,
            final HtsCodecVersion codecVersion) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(readsFormat, "Codec format must not be null");
        ValidationUtils.nonNull(codecVersion, "Codec version must not be null");
        return (T) (readsCodecs.getCodecForFormatAndVersion(readsFormat, codecVersion)
                .map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", outputPath))));
    }

    // **** Variants ******/

    @SuppressWarnings("unchecked")
    public static<T extends VariantsDecoder> T getVariantsDecoder(final IOPath inputPath) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        return (T) (variantCodecs.getCodecForIOPath(inputPath)
                .map(codec -> codec.getDecoder(inputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "variants", inputPath))));
    }

    @SuppressWarnings("unchecked")
    public static<T extends VariantsDecoder> T getVariantsDecoder(
            final IOPath inputPath,
            final VariantsDecoderOptions variantsDecoderOptions) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        ValidationUtils.nonNull(variantsDecoderOptions, "Decoder options must not be null");
        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForIOPath(inputPath);
        return (T) (variantCodec.map(codec -> codec.getDecoder(inputPath, variantsDecoderOptions))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "variants", inputPath))));
    }

    @SuppressWarnings("unchecked")
    public static<T extends VariantsEncoder> T getVariantsEncoder(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForIOPath(outputPath);
        return (T) (variantCodec.map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static<T extends VariantsEncoder> T getVariantsEncoder(
            final IOPath outputPath,
            final VariantsEncoderOptions variantsEncoderOptions) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(variantsEncoderOptions, "Encoder options must not be null");
        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForIOPath(outputPath);
        return (T) (variantCodec.map(codec -> codec.getEncoder(outputPath, variantsEncoderOptions))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static <T extends VariantsEncoder> T getVariantsEncoder(
            final IOPath outputPath,
            final VariantsFormat variantsFormat,
            final HtsCodecVersion codecVersion) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(variantsFormat, "Format must not be null");
        ValidationUtils.nonNull(codecVersion, "Codec version must not be null");
        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForFormatAndVersion(variantsFormat, codecVersion);
        return (T) (variantCodec.map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static<T extends HaploidReferenceDecoder> T getHapRefDecoder(final IOPath inputPath) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        final Optional<HaploidReferenceCodec> haploidReferenceCodec = haprefCodecs.getCodecForIOPath(inputPath);
        return (T) (haploidReferenceCodec.map(codec -> codec.getDecoder(inputPath))
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "hapref", inputPath))));
    }

//    // Once we find a codec, hand it off already primed with the version header, etc).
//    public static ReadsCodec findReadsCodecFor(final String sourceName, final InputStream is) {
//        final byte[] signatureBytes = new byte[minSignatureSize];
//        try {
//            final int numRead = is.read(signatureBytes);
//            if (numRead <= 0) {
//                throw new HtsjdkIOException(String.format("Failure reading content from stream for %s", sourceName));
//            }
//            return discoveredCodecs
//                    .values()
//                    .stream()
//                    .flatMap(m -> m.values().stream())
//                    .filter(
//                        // its possible that the input is a legitimate stream for some codec, but
//                        // contains less bytes than are required even for signature detection by another
//                        // codecs, so skip any descriptors that require more bytes than are available
//                        codecDescriptor ->
//                                numRead >= codecDescriptor.getFileSignatureSize() &&
//                                codecDescriptor.canDecode(signatureBytes))
//                    .findFirst()
//                    .orElseThrow(() -> new HtsjdkIOException(String.format("No codec found for %s", sourceName)))
//                    .getCodecInstance(is);
//        } catch (IOException e) {
//            throw new HtsjdkIOException(String.format("Failure reading signature from stream for %s", sourceName), e);
//        }
//    }
}

