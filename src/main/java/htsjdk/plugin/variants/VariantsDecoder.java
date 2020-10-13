package htsjdk.plugin.variants;

import htsjdk.plugin.HtsDecoder;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFReader;

public interface VariantsDecoder extends HtsDecoder<VCFHeader, VariantsFormat, VCFReader> {
}
