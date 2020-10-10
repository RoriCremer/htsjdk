package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsReader;
import htsjdk.plugin.UnusedType;
import htsjdk.samtools.reference.ReferenceSequenceFile;

public interface HaploidReferenceReader extends HtsReader<UnusedType, HaploidReferenceFormat, ReferenceSequenceFile> {
}
