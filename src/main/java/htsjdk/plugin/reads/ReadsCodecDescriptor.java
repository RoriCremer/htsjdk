package htsjdk.plugin.reads;

import htsjdk.plugin.HtsCodecType;
import htsjdk.plugin.HtsCodecDescriptor;

public interface ReadsCodecDescriptor extends HtsCodecDescriptor<ReadsFormat, ReadsReader, ReadsWriter> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.ALIGNED_READS; }

}
