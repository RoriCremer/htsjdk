package htsjdk.plugin.reads;

import htsjdk.plugin.HtsReader;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamReader;

// TODO: interface abstraction over SamReaderFactory/options
public interface ReadsReader extends HtsReader<SAMFileHeader, SAMRecord, SamReader> {

}
