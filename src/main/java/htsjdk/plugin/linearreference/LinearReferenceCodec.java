package htsjdk.plugin.linearreference;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;

public interface LinearReferenceCodec extends HtsCodec<LinearReferenceFormat, LinearReferenceReader, LinearReferenceWriter> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.REFERENCE; }

}
