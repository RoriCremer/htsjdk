package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;
import htsjdk.samtools.SAMSequenceDictionary;

public interface HaploidReferenceCodec extends HtsCodec<HaploidReferenceFormat> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.REFERENCE; }

}
