package htsjdk.plugin;

import java.io.Closeable;

public interface HtsEncoder<HEADER, FORMAT, WRITER> extends Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    WRITER getRecordWriter(final HEADER header);

    void close();

}
