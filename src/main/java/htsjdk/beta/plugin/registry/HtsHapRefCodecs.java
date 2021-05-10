package htsjdk.beta.plugin.registry;

import htsjdk.beta.plugin.hapref.HaploidReferenceCodec;
import htsjdk.beta.plugin.hapref.HaploidReferenceDecoder;
import htsjdk.beta.plugin.hapref.HaploidReferenceFormat;
import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.Optional;

public class HtsHapRefCodecs {
    private static HtsCodecsByFormat<HaploidReferenceFormat, HaploidReferenceCodec> haprefCodecs = HtsCodecRegistry.getHapRefCodecs();

    //TODO: validate stream signature
    @SuppressWarnings("unchecked")
    public static HaploidReferenceDecoder getHapRefDecoder(final IOPath inputPath) {
        ValidationUtils.nonNull(inputPath, "Input path must not be null");
        final Optional<HaploidReferenceCodec> haploidReferenceCodec = haprefCodecs.getCodecForInputIOPathObsolete(inputPath);

        return (HaploidReferenceDecoder) (haploidReferenceCodec.map(codec -> codec.getDecoder(inputPath))
                .orElseThrow(() -> new RuntimeException(String.format(HtsCodecRegistry.NO_CODEC_MSG_FORMAT_STRING, "hapref", inputPath))));
    }
}
