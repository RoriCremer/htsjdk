package htsjdk.beta.plugin;

import htsjdk.beta.plugin.bundle.Bundle;
import htsjdk.beta.plugin.bundle.BundleBuilder;
import htsjdk.beta.plugin.bundle.BundleResource;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.beta.plugin.bundle.BundleResourceType;
import htsjdk.beta.plugin.bundle.IOPathResource;
import htsjdk.beta.plugin.hapref.HaploidReferenceCodec;
import htsjdk.beta.plugin.hapref.HaploidReferenceFormat;
import htsjdk.beta.plugin.hapref.HaploidReferenceDecoder;

import htsjdk.beta.plugin.reads.ReadsCodec;
import htsjdk.beta.plugin.reads.ReadsDecoder;
import htsjdk.beta.plugin.reads.ReadsDecoderOptions;
import htsjdk.beta.plugin.reads.ReadsEncoder;
import htsjdk.beta.plugin.reads.ReadsEncoderOptions;
import htsjdk.beta.plugin.reads.ReadsFormat;

import htsjdk.beta.plugin.variants.VariantsCodec;
import htsjdk.beta.plugin.variants.VariantsDecoder;
import htsjdk.beta.plugin.variants.VariantsDecoderOptions;
import htsjdk.beta.plugin.variants.VariantsEncoder;
import htsjdk.beta.plugin.variants.VariantsEncoderOptions;
import htsjdk.beta.plugin.variants.VariantsFormat;

import htsjdk.utils.ValidationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

// To add a new codec type T (new interfaces):
//      define the header and record interfaces
//      define the corresponding HtsCodec, HtsEncoder and HtsDecoder subinterfaces
//      add a new HtsCodecsForType variable to the registry
//      update registerCodec to register discovered codecs of that type
//      add getEncoder and getDecoder entry points to the registry:
//
//        getTDecoder(IOPath)
//        getTDecoder(IOPath, ReadOptions)
//        getTDecoder(Bundle)
//        getTDecoder(Bundle, ReadOptions)
//        getTDecoder(Bundle, ReadOptions, HtsCodecVersion)
//
//        getTEncoder(IOPath)
//        getTEncoder(IOPath, WriteOptions)
//        getTEncoder(Bundle)
//        getTEncoder(Bundle, WriteOptions)
//        getTEncoder(Bundle, WriteOptions, HtsCodecVersion)

/**
 * Registry/cache for binding to encoders/decoders.
 */
@SuppressWarnings("rawtypes")
public class HtsCodecRegistry {
    private static final HtsCodecRegistry htsCodecRegistry = new HtsCodecRegistry();

    // maps of codec versions, by format, for each codec type
    private static HtsCodecsForType<HaploidReferenceFormat, HaploidReferenceCodec>
            haprefCodecs = new HtsCodecsForType<>();
    private static HtsCodecsForType<ReadsFormat, ReadsCodec>
            readsCodecs = new HtsCodecsForType<>();
    private static HtsCodecsForType<VariantsFormat, VariantsCodec>
            variantCodecs = new HtsCodecsForType<>();

    private final static String NO_CODEC_MSG_FORMAT_STRING = "A %s codec capable of handling \"%s\" could not be found";

    //discover any codecs on the classpath
    static { ServiceLoader.load(HtsCodec.class).forEach(htsCodecRegistry::registerCodec); }

    private HtsCodecRegistry() {}

    /**
     * Add a codec to the registry
     */
    private void registerCodec(final HtsCodec codec) {
        switch (codec.getCodecType()) {
            case ALIGNED_READS:
                readsCodecs.register((ReadsCodec) codec);
                break;

            case HAPLOID_REFERENCE:
                haprefCodecs.register((HaploidReferenceCodec) codec);
                break;

            case VARIANTS:
                variantCodecs.register((VariantsCodec) codec);
                break;

            case FEATURES:
                throw new RuntimeException("Features codec type not yet implemented");

            default:
                throw new IllegalArgumentException("Unknown codec type");
        }
    }

    // **** Reads ******/

    @SuppressWarnings("unchecked")
    public static ReadsDecoder getReadsDecoder(final IOPath inputPath) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        return getReadsDecoder(
                BundleBuilder.start()
                        .add(new IOPathResource(inputPath, BundleResourceType.READS))
                        .getBundle(),
                new ReadsDecoderOptions()
        );
    }

    @SuppressWarnings("unchecked")
    public static ReadsDecoder getReadsDecoder(
            final IOPath inputPath,
            final ReadsDecoderOptions readsDecoderOptions) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        ValidationUtils.nonNull(readsDecoderOptions, "Decoder options must not be null");
        return getReadsDecoder(
                BundleBuilder.start()
                        .add(new IOPathResource(inputPath, BundleResourceType.READS))
                        .getBundle(),
                readsDecoderOptions);
    }

    @SuppressWarnings("unchecked")
    public static ReadsDecoder getReadsDecoder(
            final Bundle inputBundle,
            final ReadsDecoderOptions readsDecoderOptions) {
        ValidationUtils.nonNull(inputBundle, "Input bundle must not be null");
        ValidationUtils.nonNull(readsDecoderOptions, "Decoder options must not be null");

        final Optional<BundleResource> readsInput = inputBundle.get(BundleResourceType.READS);
        if (!readsInput.isPresent()) {
            throw new IllegalArgumentException(String.format("No source of reads was found in input bundle", inputBundle));
        }
        // TODO: this currently assumes the input source is an IOPath; needs to handle stream, etc...
        final IOPath readsPath = readsInput.get().getIOPath().get();
        final List<ReadsCodec> codecs = readsCodecs.getCodecsForIOPath(readsPath);
        final ReadsDecoder decoder = codecs
                .stream()
                .filter(codec -> canDecodeSignature(codec, readsPath))
                .map(codec -> (ReadsDecoder) codec.getDecoder(inputBundle, readsDecoderOptions))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", readsPath)));
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
        final VariantsDecoder decoder = (VariantsDecoder) codecs
                .stream()
                .filter(codec -> canDecodeSignature(codec, inputPath))
                .map(codec -> codec.getDecoder(inputPath))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath)));
        return decoder;
    }

    @SuppressWarnings("unchecked")
    public static VariantsDecoder getVariantsDecoder(
            final IOPath inputPath,
            final VariantsDecoderOptions variantsDecoderOptions) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        ValidationUtils.nonNull(variantsDecoderOptions, "Decoder options must not be null");

        final List<VariantsCodec> codecs = variantCodecs.getCodecsForIOPath(inputPath);
        final VariantsDecoder decoder = (VariantsDecoder) codecs
                .stream()
                .filter(codec -> canDecodeSignature(codec, inputPath))
                .map(codec -> codec.getDecoder(inputPath, variantsDecoderOptions))
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
            final VariantsEncoderOptions variantsEncoderOptions) {
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
        if (inputPath.hasFileSystemProvider()) {
            try (final InputStream rawInputStream = inputPath.getInputStream()) {
                return codec.canDecodeSignature(rawInputStream, inputPath.getRawInputString());
            } catch (IOException e) {
                throw new HtsjdkIOException(String.format("Failure reading signature from stream for %s", inputPath.getRawInputString()), e);
            }
        } else {
            return codec.canDecodeURI(inputPath);
        }
    }

}

