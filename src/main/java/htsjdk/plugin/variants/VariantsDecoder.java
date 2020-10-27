package htsjdk.plugin.variants;

import htsjdk.plugin.HtsDecoder;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFHeader;

public interface VariantsDecoder extends HtsDecoder<VCFHeader, VariantsFormat, VariantContext> { }
