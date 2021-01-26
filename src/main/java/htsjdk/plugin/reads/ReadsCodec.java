package htsjdk.plugin.reads;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;

/**
 * Base class for all reads codecs.
 */
public interface ReadsCodec extends HtsCodec<ReadsFormat, ReadsDecoderOptions, ReadsEncoderOptions> {

    @Override
    default HtsCodecType getCodecType() { return HtsCodecType.ALIGNED_READS; }

}
