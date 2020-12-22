package htsjdk.plugin.reads;

import htsjdk.plugin.HtsEncoder;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;

/**
 * Base interface for reads encoders.
 */
public interface ReadsEncoder extends HtsEncoder<ReadsFormat, SAMFileHeader, SAMRecord> { }
