package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsEncoder;
import htsjdk.plugin.UnusedType;
import htsjdk.samtools.reference.FastaReferenceWriter;

public interface HaploidReferenceEncoder extends HtsEncoder<UnusedType, HaploidReferenceFormat, FastaReferenceWriter> { }
