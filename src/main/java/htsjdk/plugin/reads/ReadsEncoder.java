package htsjdk.plugin.reads;

import htsjdk.plugin.HtsEncoder;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;

public interface ReadsEncoder extends HtsEncoder<SAMFileHeader, ReadsFormat, SAMFileWriter> { }
