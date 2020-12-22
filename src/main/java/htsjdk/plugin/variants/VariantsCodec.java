package htsjdk.plugin.variants;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecCategory;

public interface VariantsCodec extends HtsCodec<VariantsFormat, VariantsDecoderOptions, VariantsEncoderOptions> {

    @Override
    default HtsCodecCategory getCodecCategory() { return HtsCodecCategory.VARIANTS; }

}
