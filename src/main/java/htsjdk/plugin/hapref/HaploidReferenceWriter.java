package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsWriter;
import htsjdk.plugin.UnusedType;
import htsjdk.samtools.reference.FastaReferenceWriter;

public interface HaploidReferenceWriter extends HtsWriter<UnusedType, HaploidReferenceFormat, FastaReferenceWriter> { }
