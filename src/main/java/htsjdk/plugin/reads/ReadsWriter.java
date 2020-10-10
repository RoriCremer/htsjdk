package htsjdk.plugin.reads;

import htsjdk.plugin.HtsWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;

public interface ReadsWriter extends HtsWriter<SAMFileHeader, ReadsFormat, SAMFileWriter> { }
