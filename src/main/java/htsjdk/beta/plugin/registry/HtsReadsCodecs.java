package htsjdk.beta.plugin.registry;

import htsjdk.beta.plugin.HtsCodecVersion;
import htsjdk.beta.plugin.bundle.Bundle;
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

public class HtsReadsCodecs {
    //private static final Log LOG = Log.getInstance(HtsReadsCodecs.class);

    private static HtsCodecsByFormat<ReadsFormat, ReadsCodec> readsCodecs = HtsCodecRegistry.getReadsCodecs();

    HtsReadsCodecs() {}

    //***********************
    // Decoders

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
    public static ReadsDecoder getReadsDecoder(final Bundle inputBundle) {
        ValidationUtils.nonNull(inputBundle, "Input bundle must not be null");
        return getReadsDecoder(inputBundle, new ReadsDecoderOptions());
    }

    @SuppressWarnings("unchecked")
    public static ReadsDecoder getReadsDecoder(
            final Bundle inputBundle,
            final ReadsDecoderOptions readsDecoderOptions) {
        ValidationUtils.nonNull(inputBundle, "Input bundle must not be null");
        ValidationUtils.nonNull(readsDecoderOptions, "Decoder options must not be null");

        final ReadsCodec readsCodec = readsCodecs.resolveCodecForInput(
                inputBundle,
                BundleResourceType.READS,
                HtsReadsCodecs::mapSubContentTypeToReadsFormat);
        return (ReadsDecoder) readsCodec.getDecoder(inputBundle, readsDecoderOptions);
    }

    //***********************
    // Encoders

    @SuppressWarnings("unchecked")
    public static ReadsEncoder getReadsEncoder(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");

        return (ReadsEncoder) readsCodecs.getCodecForInputIOPathObsolete(outputPath)
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

        return (ReadsEncoder) readsCodecs.getCodecForInputIOPathObsolete(outputPath)
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

        final ReadsCodec readsCodec = readsCodecs.getCodecForFormatAndVersion(readsFormat, codecVersion);
        return (ReadsEncoder) readsCodec.getEncoder(outputPath);
    }

//    @SuppressWarnings("unchecked")
//    public static ReadsEncoder getReadsEncoder(
//            final IOPath outputPath,
//            final ReadsFormat readsFormat,
//            final HtsCodecVersion requestedVersion,
//            final HtsCodecVersion codecVersion) {
//    }

//    @SuppressWarnings("unchecked")
//    public static ReadsEncoder getReadsEncoder(
//            final Bundle outputBundle,
//            final ReadsFormat readsFormat,
//            final HtsCodecVersion requestedVersion,
//            final HtsCodecVersion codecVersion) {
//    }

    //TODO:move this to ReadsFormat ?
    static ReadsFormat mapSubContentTypeToReadsFormat(final String contentSubType) {
        ValidationUtils.nonNull(contentSubType, "contentSubType");
        for (final ReadsFormat f : ReadsFormat.values()) {
            if (f.name().equals(contentSubType)) {
                return f;
            }
        }
        return null;
    }

}


