package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsEncoder;
import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.samtools.reference.ReferenceSequence;

public interface HaploidReferenceEncoder extends HtsEncoder<SAMSequenceDictionary, HaploidReferenceFormat, ReferenceSequence> { }
