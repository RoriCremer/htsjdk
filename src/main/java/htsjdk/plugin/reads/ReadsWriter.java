package htsjdk.plugin.reads;

import htsjdk.plugin.HtsWriter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMRecord;

// TODO: version ? version as a type param ??
// interface/abstraction over SAMFileWriterFactory
public interface ReadsWriter extends HtsWriter<SAMFileHeader, SAMRecord, SAMFileWriter> {

}
