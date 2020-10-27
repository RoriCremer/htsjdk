package htsjdk.plugin;

import java.io.Closeable;

public interface HtsEncoder<HEADER extends HtsHeader, FORMAT, RECORD extends HtsRecord> extends Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    void setHeader(HEADER header);

    void write(RECORD record);

    void close();

}
