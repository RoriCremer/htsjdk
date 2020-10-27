package htsjdk.plugin;

import java.io.Closeable;

public interface HtsEncoder<FORMAT, HEADER extends HtsHeader, RECORD extends HtsRecord> extends Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    void setHeader(HEADER header);

    void write(RECORD record);

    void close();

}
