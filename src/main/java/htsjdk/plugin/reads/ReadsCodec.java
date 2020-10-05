package htsjdk.plugin.reads;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;
import htsjdk.plugin.HtsCodecVersion;

public interface ReadsCodec extends HtsCodec<ReadsFormat, ReadsReader, ReadsWriter> {

    HtsCodecVersion BAM_DEFAULT_VERSION = new HtsCodecVersion(1, 0,0);

    @Override
    default HtsCodecType getType() { return HtsCodecType.ALIGNED_READS; }

}
