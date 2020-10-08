package htsjdk.plugin.reads;

import htsjdk.plugin.HtsReader;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

public interface ReadsReader extends HtsReader<SAMFileHeader, SamReaderFactory, SamReader> {

}
