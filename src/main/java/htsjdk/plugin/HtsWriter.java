package htsjdk.plugin;

import java.io.Closeable;

public interface HtsWriter<HEADER, RECORD, RECORD_WRITER>  extends Closeable {

    RECORD_WRITER getRecordWriter(final HEADER samHeader);

    void close();

}
