package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;

public interface HaploidReferenceCodec extends HtsCodec<HaploidReferenceFormat, HaploidReferenceReader, HaploidReferenceWriter> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.REFERENCE; }

}
