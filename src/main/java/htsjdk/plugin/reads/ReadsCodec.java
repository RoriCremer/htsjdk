package htsjdk.plugin.reads;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;

public interface ReadsCodec extends HtsCodec<ReadsFormat, ReadsReader, ReadsWriter> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.ALIGNED_READS; }

}
