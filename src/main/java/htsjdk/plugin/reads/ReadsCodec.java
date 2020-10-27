package htsjdk.plugin.reads;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;
import htsjdk.samtools.SAMFileHeader;

public interface ReadsCodec extends HtsCodec<ReadsFormat, ReadsDecoderOptions, ReadsEncoderOptions> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.ALIGNED_READS; }

}
