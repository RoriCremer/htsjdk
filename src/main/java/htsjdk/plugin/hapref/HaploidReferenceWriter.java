package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsWriter;
import htsjdk.samtools.reference.FastaReferenceWriter;

// TODO: reference format has neither a header nor an options factory
public interface HaploidReferenceWriter extends HtsWriter<Object, Object, FastaReferenceWriter> {

}
