package htsjdk.plugin.variants;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;
import htsjdk.variant.vcf.VCFHeader;

public interface VariantsCodec extends HtsCodec<VariantsFormat> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.VARIANTS; }

}
