package htsjdk.plugin;

import java.io.Closeable;

public interface HtsReader<HEADER, FORMAT, RECORD_READER> extends Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    RECORD_READER getRecordReader();

    HEADER getHeader();

    void close();

}
