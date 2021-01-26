package htsjdk.plugin.reads;

import htsjdk.plugin.HtsDecoder;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;

public interface ReadsDecoder2 extends HtsDecoder<ReadsFormat, SAMFileHeader, SAMRecord>, ReadsQuery<SAMRecord> {

}
