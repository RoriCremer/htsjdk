package htsjdk.plugin;

import htsjdk.plugin.interval.HtsQuery;

import java.io.Closeable;

// TODO:
// - Do we need to retain the existing limitation of a single open iterator (and thus CloseableIterator)
//   and reject an attempt to open a second one, or just cancel any outstanding iterator on a subsequent query ?
// - Should we move "extends HtsQuery" down one level to the individual base classes (i.e., ReadsCodec, VariantsCodec?)

/**
 * Base interface for decoders.
 *
 * @param <F> enum representing the formats for this codec category
 *               (i.e., ReadsFormat defining SAM/BAM/CRAM constants)
 * @param <H> type param for the header for this format (i.e. SAMFileHeader)
 * @param <R> type param for the record for this format (i.e. SAMRecord)
 */
public interface HtsDecoder<F extends Enum<F>, H extends HtsHeader, R extends HtsRecord>
        extends HtsQuery<R>, Closeable {

    F getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    H getHeader();

    void close();
}
