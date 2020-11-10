package htsjdk.plugin.variants;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;

public interface VariantsCodec extends HtsCodec<VariantsFormat, VariantsBundle, VariantsDecoderOptions, VariantsEncoderOptions> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.VARIANTS; }

}
