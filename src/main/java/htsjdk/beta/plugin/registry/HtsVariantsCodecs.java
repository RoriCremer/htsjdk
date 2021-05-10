package htsjdk.beta.plugin.registry;

import htsjdk.beta.plugin.HtsCodecVersion;
import htsjdk.beta.plugin.variants.VariantsCodec;
import htsjdk.beta.plugin.variants.VariantsDecoder;
import htsjdk.beta.plugin.variants.VariantsDecoderOptions;
import htsjdk.beta.plugin.variants.VariantsEncoder;
import htsjdk.beta.plugin.variants.VariantsEncoderOptions;
import htsjdk.beta.plugin.variants.VariantsFormat;
import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class HtsVariantsCodecs {
    private static HtsCodecsByFormat<VariantsFormat, VariantsCodec> variantCodecs = HtsCodecRegistry.getVariantCodecs();

    @SuppressWarnings("unchecked")
    public static VariantsDecoder getVariantsDecoder(final IOPath inputPath) {
        return getVariantsDecoder(inputPath, new VariantsDecoderOptions());
    }

    @SuppressWarnings("unchecked")
    public static VariantsDecoder getVariantsDecoder(
            final IOPath inputPath,
            final VariantsDecoderOptions variantsDecoderOptions) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        ValidationUtils.nonNull(variantsDecoderOptions, "Decoder options must not be null");

        final List<VariantsCodec> codecs = variantCodecs.getCodecsForInputIOPathObsolete(inputPath);
        final VariantsDecoder decoder = (VariantsDecoder) codecs
                .stream()
                .filter(codec -> HtsCodecsByFormat.canDecodeIOPathSignature(codec, inputPath))
                .map(codec -> codec.getDecoder(inputPath, variantsDecoderOptions))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath)));
        return decoder;
// enabling this requires updating the decoder to use the input bundle
//        final Bundle inputBundle = BundleBuilder.start()
//                .addPrimary(new IOPathResource(inputPath, BundleResourceType.VARIANTS)).getBundle();
//        final VariantsCodec variantsCodec = variantCodecs.resolveInputCodec(
//                inputBundle,
//                BundleResourceType.VARIANTS,
//                HtsVariantsCodecs::mapVariantsFormatToSubContentType);
//        return (VariantsDecoder) variantsCodec.getDecoder(inputBundle, variantsDecoderOptions);
    }

    @SuppressWarnings("unchecked")
    public static VariantsEncoder getVariantsEncoder(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");

        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForInputIOPathObsolete(outputPath);
        return (VariantsEncoder) (variantCodec.map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static VariantsEncoder getVariantsEncoder(
            final IOPath outputPath,
            final VariantsEncoderOptions variantsEncoderOptions) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(variantsEncoderOptions, "Encoder options must not be null");

        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForInputIOPathObsolete(outputPath);
        return (VariantsEncoder) (variantCodec.map(codec -> codec.getEncoder(outputPath, variantsEncoderOptions))
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static VariantsEncoder getVariantsEncoder(
            final IOPath outputPath,
            final VariantsFormat variantsFormat,
            final HtsCodecVersion codecVersion) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(variantsFormat, "Format must not be null");
        ValidationUtils.nonNull(codecVersion, "Codec version must not be null");

        final VariantsCodec variantCodec = variantCodecs.getCodecForFormatAndVersion(variantsFormat, codecVersion);
        return (VariantsEncoder) variantCodec.getEncoder(outputPath);
    }

    //TODO:move this to VariantsFormat ?
    static VariantsFormat mapVariantsFormatToSubContentType(final String subContentType) {
        ValidationUtils.nonNull(subContentType, "subContentType");
        for (final VariantsFormat f : VariantsFormat.values()) {
            if (f.name().equals(subContentType)) {
                return f;
            }
        }
        return null;
    }

}
