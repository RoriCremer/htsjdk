package htsjdk.plugin.reads;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecCategory;

/**
 * Base class for all reads codecs.
 */
public interface ReadsCodec extends HtsCodec<ReadsFormat, ReadsDecoderOptions, ReadsEncoderOptions> {

    @Override
    default HtsCodecCategory getCodecCategory() { return HtsCodecCategory.ALIGNED_READS; }

}
