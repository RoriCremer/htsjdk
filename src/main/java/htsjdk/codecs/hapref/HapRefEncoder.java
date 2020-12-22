package htsjdk.codecs.hapref;

import htsjdk.plugin.hapref.HaploidReferenceEncoder;

/**
 * Base class for haploid reference encoders.
 */
public abstract class HapRefEncoder implements HaploidReferenceEncoder {

    public HapRefEncoder() {
        throw new IllegalStateException("Not implemented");
    }

}
