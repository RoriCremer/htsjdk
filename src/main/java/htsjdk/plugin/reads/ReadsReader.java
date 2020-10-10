package htsjdk.plugin.reads;

import htsjdk.plugin.HtsReader;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SamReader;

public interface ReadsReader extends HtsReader<SAMFileHeader, ReadsFormat, SamReader> { }
