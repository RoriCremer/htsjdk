package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsDecoder;
import htsjdk.plugin.UnusedType;
import htsjdk.samtools.reference.ReferenceSequenceFile;

public interface HaploidReferenceDecoder extends HtsDecoder<UnusedType, HaploidReferenceFormat, ReferenceSequenceFile> {
}
