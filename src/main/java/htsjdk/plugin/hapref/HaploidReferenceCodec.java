package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecCategory;
import htsjdk.plugin.HtsDecoderOptions;
import htsjdk.plugin.HtsEncoderOptions;

//TODO:should this use a more specific HaploidReferenceOptions class, even if its a no-op

public interface HaploidReferenceCodec extends HtsCodec<HaploidReferenceFormat, HtsDecoderOptions, HtsEncoderOptions> {

    @Override
    default HtsCodecCategory getCodecCategory() { return HtsCodecCategory.HAPLOID_REFERENCE; }

}
