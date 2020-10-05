package htsjdk.plugin.reads;

import htsjdk.plugin.HtsWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMFileWriterFactory;

public interface ReadsWriter extends HtsWriter<SAMFileHeader, SAMFileWriterFactory, SAMFileWriter> {
}
