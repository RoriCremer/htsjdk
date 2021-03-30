package htsjdk.beta.codecs.variants.vcf;

import htsjdk.io.IOPath;
import htsjdk.beta.plugin.variants.VariantsEncoder;
import htsjdk.beta.plugin.variants.VariantsFormat;

import java.io.OutputStream;

public abstract class VCFEncoder implements VariantsEncoder {
    final private String displayName;
    protected IOPath outputPath;
    protected OutputStream os;

    public VCFEncoder(final IOPath outputPath) {
        this.outputPath = outputPath;
        this.displayName = outputPath.getRawInputString();
    }

    public VCFEncoder(final OutputStream os, final String displayName) {
        this.os = os;
        this.displayName = displayName;
    }

    @Override
    final public VariantsFormat getFormat() { return VariantsFormat.VCF; }

    @Override
    final public String getDisplayName() { return displayName; }

}
