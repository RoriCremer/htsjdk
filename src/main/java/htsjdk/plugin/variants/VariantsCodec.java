package htsjdk.plugin.variants;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;

public interface VariantsCodec extends HtsCodec<VariantsFormat, VariantsDecoderOptions, VariantsEncoderOptions> {

    @Override
    default HtsCodecType getCodecType() { return HtsCodecType.VARIANTS; }

}
