package htsjdk.plugin.reads;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;

public interface ReadsCodec extends HtsCodec<ReadsFormat, ReadsEncoder, ReadsEncoderOptions, ReadsDecoder, ReadsDecoderOptions> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.ALIGNED_READS; }

}
