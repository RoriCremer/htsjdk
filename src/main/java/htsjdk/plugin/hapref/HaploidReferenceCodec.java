package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;
import htsjdk.plugin.HtsDecoderOptions;
import htsjdk.plugin.HtsEncoderOptions;

public interface HaploidReferenceCodec extends HtsCodec<HaploidReferenceFormat, HtsDecoderOptions, HtsEncoderOptions> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.REFERENCE; }

}
