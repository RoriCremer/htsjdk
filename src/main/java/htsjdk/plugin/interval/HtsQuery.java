package htsjdk.plugin.interval;

import java.util.Iterator;
import java.util.List;

// TODO:
// - coord system interpretation is ambiguous (and different across decoders)
// - wild cards, i.e., end of reference/contig
// - why does SamReader have query(..., contained) AND queryContained ?

/**
 * Common query interface for decoders
 * @param <RECORD>
 */
public interface HtsQuery<RECORD> {

    Iterator<RECORD> iterator();
    boolean isQueryable();
    boolean hasIndex();

    Iterator<RECORD> query(String queryName, long start, long end, boolean contained);
    Iterator<RECORD> queryOverlapping(String queryName, long start, long end);
    Iterator<RECORD> queryContained(String queryName, long start, long end);

    Iterator<RECORD> query(HtsInterval interval, boolean contained);
    Iterator<RECORD> queryOverlapping(HtsInterval interval);
    Iterator<RECORD> queryContained(HtsInterval interval);

    Iterator<RECORD> query(List<HtsInterval> intervals, boolean contained);
    Iterator<RECORD> queryOverlapping(List<HtsInterval> intervals);
    Iterator<RECORD> queryContained(List<HtsInterval> intervals);

    Iterator<RECORD> queryStart(String queryName, long start);

    // Other methods from SamReader:

    // move these to a SAM-specific interface
    //Iterator<RECORD> queryUnmapped();

    // Fetch the mate for the given read.
    //RECORD queryMate(RECORD rec);
}
