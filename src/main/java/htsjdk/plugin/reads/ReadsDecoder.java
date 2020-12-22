package htsjdk.plugin.reads;

import htsjdk.plugin.HtsDecoder;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;

/**
 * Base class for all reads decoders.
 */
public interface ReadsDecoder extends HtsDecoder<ReadsFormat, SAMFileHeader, SAMRecord>, ReadsQuery { }
