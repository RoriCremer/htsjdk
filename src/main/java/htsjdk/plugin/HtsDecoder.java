package htsjdk.plugin;

import htsjdk.plugin.interval.HtsInterval;
import htsjdk.plugin.interval.HtsQuery;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;

// TODO:
// - Should these readers include a coordinate system type/class hierarchy) ?
//      the "long" type in the query* methods and in "HtsInterval" are ambiguous
// - Should the type of "queryName" be a generic type param to support non-string selectors, to
//      line up with a generic type for selector in HtsInterval ? i.e. int reference Index
// - Do we need to retain the existing limitation of a single open iterator (and thus CloseableIterator),
//   or should we just cancel any outstanding iterator on a subsequent query ?
// - Should we remove these from here, and have a getQueryable method that returns the random access interface ?

public interface HtsDecoder<FORMAT, HEADER extends HtsHeader, RECORD extends HtsRecord>
        extends Iterable<RECORD>, HtsQuery<RECORD>, Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    HEADER getHeader();

    void close();

    //*******************************************
    // Start temporary common query interface default implementations.

    @Override
    Iterator<RECORD> iterator();

    @Override
    default boolean isQueryable() { return false; }

    @Override
    default boolean hasIndex() { return false; }

    @Override
    default Iterator<RECORD> query(String queryName, long start, long end, boolean contained) { return null; }
    @Override
    default Iterator<RECORD> queryOverlapping(String queryName, long start, long end) { return null; }
    @Override
    default Iterator<RECORD> queryContained(String queryName, long start, long end) { return null; }

    @Override
    default Iterator<RECORD> query(HtsInterval interval, boolean contained) { return null; }
    @Override
    default Iterator<RECORD> queryOverlapping(HtsInterval interval) { return null; }
    @Override
    default Iterator<RECORD> queryContained(HtsInterval interval) { return null; }

    @Override
    default Iterator<RECORD> query(List<HtsInterval> intervals, boolean contained) { return null; }
    @Override
    default Iterator<RECORD> queryOverlapping(List<HtsInterval> intervals) { return null; }
    @Override
    default Iterator<RECORD> queryContained(List<HtsInterval> intervals) { return null; }

    @Override
    default Iterator<RECORD> queryStart(String queryName, long start) { return null; }

}
