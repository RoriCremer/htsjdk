package htsjdk.plugin;

import java.io.Closeable;

public interface HtsWriter<HEADER, FORMAT, RECORD_WRITER> extends Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    RECORD_WRITER getRecordWriter(final HEADER header);

    void close();

}
