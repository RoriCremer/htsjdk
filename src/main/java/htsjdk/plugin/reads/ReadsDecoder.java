package htsjdk.plugin.reads;

import htsjdk.plugin.HtsDecoder;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;

public interface ReadsDecoder extends HtsDecoder<SAMFileHeader, ReadsFormat, SAMRecord> { }
