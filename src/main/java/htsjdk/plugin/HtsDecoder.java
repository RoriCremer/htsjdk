package htsjdk.plugin;

import java.io.Closeable;

public interface HtsDecoder<HEADER extends HtsHeader, FORMAT, RECORD extends HtsRecord> extends Iterable<RECORD>, Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    HEADER getHeader();

    void close();

}
