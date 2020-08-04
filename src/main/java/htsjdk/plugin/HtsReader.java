package htsjdk.plugin;

import java.io.Closeable;

public interface HtsReader<HEADER, RECORD, RECORD_READER> extends Closeable {

    RECORD_READER getRecordReader();

    HEADER getHeader();

    void close();

}
