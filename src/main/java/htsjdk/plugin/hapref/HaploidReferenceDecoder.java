package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsDecoder;
import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.samtools.reference.ReferenceSequence;

public interface HaploidReferenceDecoder extends HtsDecoder<SAMSequenceDictionary, HaploidReferenceFormat, ReferenceSequence> { }
