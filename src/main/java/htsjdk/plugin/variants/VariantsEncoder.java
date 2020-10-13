package htsjdk.plugin.variants;

import htsjdk.plugin.HtsEncoder;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFHeader;

public interface VariantsEncoder extends HtsEncoder<VCFHeader, VariantsFormat, VariantContextWriter> {
}
