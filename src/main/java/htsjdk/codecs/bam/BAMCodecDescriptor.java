package htsjdk.codecs.bam;

import htsjdk.plugin.reads.ReadsCodecDescriptor;
import htsjdk.plugin.reads.ReadsFormat;

public abstract class BAMCodecDescriptor implements ReadsCodecDescriptor {

    @Override
    public ReadsFormat getFormat() { return ReadsFormat.BAM; }
}
