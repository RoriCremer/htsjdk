package htsjdk.plugin.reads;

import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecType;
import htsjdk.samtools.SAMFileWriterFactory;
import htsjdk.samtools.SamReaderFactory;

public interface ReadsCodec extends HtsCodec<ReadsFormat, SAMFileWriterFactory, ReadsEncoder, SamReaderFactory, ReadsDecoder> {

    @Override
    default HtsCodecType getType() { return HtsCodecType.ALIGNED_READS; }

}
