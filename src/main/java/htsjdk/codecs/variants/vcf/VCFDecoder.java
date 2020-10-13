package htsjdk.codecs.variants.vcf;

import htsjdk.io.IOPath;
import htsjdk.plugin.variants.VariantsDecoder;
import htsjdk.plugin.variants.VariantsFormat;

import java.io.InputStream;

public abstract class VCFDecoder implements VariantsDecoder {
    private final String displayName;

    protected IOPath inputPath;
    protected InputStream is;

    public VCFDecoder(final IOPath inputPath) {
        this.inputPath = inputPath;
        this.displayName = inputPath.getRawInputString();
    }

    public VCFDecoder(final InputStream is, final String displayName) {
        this.is = is;
        this.displayName = displayName;
    }

    @Override
    final public VariantsFormat getFormat() { return VariantsFormat.VCF; }

    @Override
    final public String getDisplayName() { return displayName; }

}
