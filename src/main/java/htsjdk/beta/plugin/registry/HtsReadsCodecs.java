package htsjdk.beta.plugin.registry;

import htsjdk.beta.plugin.HtsCodecVersion;
import htsjdk.beta.plugin.bundle.Bundle;
import htsjdk.beta.plugin.bundle.BundleResource;
import htsjdk.beta.plugin.bundle.BundleResourceType;
import htsjdk.beta.plugin.reads.ReadsBundle;
import htsjdk.beta.plugin.reads.ReadsCodec;
import htsjdk.beta.plugin.reads.ReadsFormat;
import htsjdk.beta.plugin.reads.ReadsDecoder;
import htsjdk.beta.plugin.reads.ReadsDecoderOptions;
import htsjdk.beta.plugin.reads.ReadsEncoder;
import htsjdk.beta.plugin.reads.ReadsEncoderOptions;
import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class HtsReadsCodecs {

    private static HtsCodecsByFormat<ReadsFormat, ReadsCodec> readsCodecs = HtsCodecRegistry.getReadsCodecs();

    HtsReadsCodecs() {}

    @SuppressWarnings("unchecked")
    public static ReadsDecoder getReadsDecoder(final IOPath inputPath) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        return getReadsDecoder(new ReadsBundle(inputPath), new ReadsDecoderOptions());
    }

    @SuppressWarnings("unchecked")
    public static ReadsDecoder getReadsDecoder(
            final IOPath inputPath,
            final ReadsDecoderOptions readsDecoderOptions) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        ValidationUtils.nonNull(readsDecoderOptions, "Decoder options must not be null");
        return getReadsDecoder(new ReadsBundle(inputPath), readsDecoderOptions);
    }

    @SuppressWarnings("unchecked")
    public static ReadsDecoder getReadsDecoder(
            final Bundle inputBundle,
            final ReadsDecoderOptions readsDecoderOptions) {
        ValidationUtils.nonNull(inputBundle, "Input bundle must not be null");
        ValidationUtils.nonNull(readsDecoderOptions, "Decoder options must not be null");

        final Optional<BundleResource> readsInput = inputBundle.get(BundleResourceType.READS);
        if (!readsInput.isPresent()) {
            throw new IllegalArgumentException(String.format("No source of reads was found in input bundle",
            inputBundle));
        }
        // TODO: this currently assumes the input source is an IOPath; needs to handle stream, etc...
        final IOPath readsPath = readsInput.get().getIOPath().get();
        final List<ReadsCodec> codecs = readsCodecs.getCodecsForIOPath(readsPath);
        final ReadsDecoder decoder = codecs
                .stream()
                .filter(codec -> HtsCodecRegistry.canDecodeSignature(codec, readsPath))
                .map(codec -> (ReadsDecoder) codec.getDecoder(inputBundle, readsDecoderOptions))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "reads",
                readsPath)));
        return decoder;
    }

    @SuppressWarnings("unchecked")
    public static ReadsEncoder getReadsEncoder(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");

        return (ReadsEncoder) readsCodecs.getCodecForIOPath(outputPath)
                .map(codec -> codec.getEncoder(outputPath))
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "reads",
                outputPath)));
    }

    @SuppressWarnings("unchecked")
    public static ReadsEncoder getReadsEncoder(
            final IOPath outputPath,
            final ReadsEncoderOptions readsEncoderOptions) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        ValidationUtils.nonNull(readsEncoderOptions, "Encoder options must not be null");

        return (ReadsEncoder) readsCodecs.getCodecForIOPath(outputPath)
                .map(codec -> codec.getEncoder(outputPath, readsEncoderOptions))
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "reads",
                outputPath)));
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
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "reads",
                outputPath))));
    }

}


