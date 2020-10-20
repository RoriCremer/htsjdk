package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;
import htsjdk.plugin.UnusedType;

// Placeholder class for unused type parameters
public interface HaploidReferenceCodec extends HtsCodec<HaploidReferenceFormat, UnusedType, HaploidReferenceEncoder, UnusedType, HaploidReferenceDecoder> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.REFERENCE; }

}
