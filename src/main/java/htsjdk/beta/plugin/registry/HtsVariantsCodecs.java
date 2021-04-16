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
        ValidationUtils.nonNull(inputPath, "Input path must not be null");

        final List<VariantsCodec> codecs = variantCodecs.getCodecsForIOPath(inputPath);
        final VariantsDecoder decoder = (VariantsDecoder) codecs
                .stream()
                .filter(codec -> HtsCodecRegistry.canDecodeSignature(codec, inputPath))
                .map(codec -> codec.getDecoder(inputPath))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath)));
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
                .filter(codec -> HtsCodecRegistry.canDecodeSignature(codec, inputPath))
                .map(codec -> codec.getDecoder(inputPath, variantsDecoderOptions))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "reads", inputPath)));
        return decoder;
    }

    @SuppressWarnings("unchecked")
    public static VariantsEncoder getVariantsEncoder(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");

        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForIOPath(outputPath);
        return (VariantsEncoder) (variantCodec.map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

    @SuppressWarnings("unchecked")
    public static VariantsEncoder getVariantsEncoder(
            final IOPath outputPath,
            final VariantsEncoderOptions variantsEncoderOptions) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(variantsEncoderOptions, "Encoder options must not be null");

        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForIOPath(outputPath);
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

        final Optional<VariantsCodec> variantCodec = variantCodecs.getCodecForFormatAndVersion(variantsFormat, codecVersion);
        return (VariantsEncoder) (variantCodec.map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "variants", outputPath))));
    }

}
