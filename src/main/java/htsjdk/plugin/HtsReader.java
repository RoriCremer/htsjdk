package htsjdk.plugin;

import java.io.Closeable;

public interface HtsReader<HEADER, READ_OPTIONS, RECORD_READER> extends Closeable {

    String getDisplayName();

    RECORD_READER getRecordReader();

    RECORD_READER getRecordReader(final READ_OPTIONS options);

    HEADER getHeader();

    void close();

}
