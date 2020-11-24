package htsjdk.plugin;

import htsjdk.plugin.interval.HtsQuery;

import java.io.Closeable;

// TODO:
// - Do we need to retain the existing limitation of a single open iterator (and thus CloseableIterator)
//   and reject an attempt to open a second one, or just cancel any outstanding iterator on a subsequent query ?
// - Should we move "extends HtsQuery" down one level to the individual base classes (i.e., ReadsCodec, VariantsCodec?)

public interface HtsDecoder<FORMAT, HEADER extends HtsHeader, RECORD extends HtsRecord>
        extends HtsQuery<RECORD>, Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    HEADER getHeader();

    void close();
}
