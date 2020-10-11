package htsjdk.plugin;

import java.io.Closeable;

public interface HtsDecoder<HEADER, FORMAT, READER> extends Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    READER getRecordReader();

    HEADER getHeader();

    void close();

}
