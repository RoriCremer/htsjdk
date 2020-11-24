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
import htsjdk.utils.ValidationUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HtsInterval {

    private final String queryName;
    private final long start;
    private final long end;

    public HtsInterval(final String queryName, final long start, final long end){
        //validatePositions(contig, start, end);
        this.queryName = queryName;
        this.start = start;
        this.end = end;
    }

    public HtsInterval(final QueryInterval queryInterval, final SAMSequenceDictionary dictionary) {
        ValidationUtils.nonNull(dictionary, "a valid sequence dictionary is required");
        ValidationUtils.nonNull(dictionary.getSequence(queryInterval.referenceIndex),
                String.format("query index %d is not present in the provided dictionary", queryInterval.referenceIndex));
        ValidationUtils.nonNull(dictionary.getSequence(queryInterval.referenceIndex).getContig(),
                String.format("contig name for index %d is not present in the provided dictionary", queryInterval.referenceIndex));
        this.queryName = dictionary.getSequence(queryInterval.referenceIndex).getContig();
        this.start = queryInterval.start;
        this.end = queryInterval.end;
    }

    public String getQueryName() { return queryName; }

    public long getStart() { return start; }

    public long getEnd() { return end; }

    public QueryInterval toQueryInterval(final SAMSequenceDictionary dictionary) {
        return new QueryInterval(
                dictionary.getSequenceIndex(getQueryName()),
                toIntegerSafe(getStart()),
                toIntegerSafe(getEnd()));
    }

    public static QueryInterval[] toQueryIntervalArray(
            final List<HtsInterval> intervals,
            final SAMSequenceDictionary dictionary) {
        return intervals
                .stream()
                .map(si -> si.toQueryInterval(dictionary))
                .collect(Collectors.toList()).toArray(new QueryInterval[intervals.size()]);
    }

    public static List<HtsInterval> fromQueryIntervalArray(
            final QueryInterval[] queryIntervals,
            final SAMSequenceDictionary dictionary) {
        return Arrays.stream(queryIntervals)
                .map(si -> new HtsInterval(si, dictionary))
                .collect(Collectors.toList());
    }

    public static int toIntegerSafe(final long coord) {
        try {
            return Math.toIntExact(coord);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(String.format("long to int conversion of %d results in integer overflow", coord), e);
        }
    }
}
