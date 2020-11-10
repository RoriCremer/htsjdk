package htsjdk.plugin.interval;

// TODO:
// Final:
// use a single, normalized, 1 based, closed coordinate system
// use SimpleInterval if possible, only support String queryName
// - Wild cards, i.e., end of reference/contig
//   ?? wildcards, use an interval that spans
// Add a query by String (ie, by readname)
// for special (per-reader) cases, implement an extended interface (ie., queryUnMapped)

import htsjdk.samtools.QueryInterval;
import htsjdk.samtools.SAMSequenceDictionary;

import java.util.List;
import java.util.stream.Collectors;

public interface HtsInterval {

    String getQueryName();

    long getStart();

    long getEnd();

    default QueryInterval toQueryInterval(final SAMSequenceDictionary dictionary) {
        return new QueryInterval(
                dictionary.getSequenceIndex(getQueryName()),
                toIntegerSafe(getStart()),
                toIntegerSafe(getEnd()));
    }

    static QueryInterval[] asQueryIntervalArray(
            final List<HtsInterval> intervals,
            final SAMSequenceDictionary dictionary) {
        return intervals
                .stream()
                .map(si -> si.toQueryInterval(dictionary))
                .collect(Collectors.toList()).toArray(new QueryInterval[intervals.size()]);
    }

    static int toIntegerSafe(final long coord) {
        try {
            return Math.toIntExact(coord);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(String.format("long to in conversion of %d results in integer overflow", coord), e);
        }
    }
}
