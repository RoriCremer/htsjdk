package htsjdk.plugin;

import java.io.Closeable;

public interface HtsWriter<HEADER, WRITE_OPTIONS, RECORD_WRITER>  extends Closeable {

    String getDisplayName();

    RECORD_WRITER getRecordWriter(final HEADER samHeader);

    RECORD_WRITER getRecordWriter(final HEADER samHeader, final WRITE_OPTIONS options);

    void close();

}
