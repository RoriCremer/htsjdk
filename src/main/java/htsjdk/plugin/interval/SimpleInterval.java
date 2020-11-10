package htsjdk.plugin.interval;

import htsjdk.samtools.QueryInterval;
import htsjdk.samtools.SAMSequenceDictionary;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 1-based closed interval (codec implementations will translate as needed)
 */
public class SimpleInterval implements HtsInterval {

    private final String queryName;
    private final long start;
    private final long end;

    public SimpleInterval(final String queryName, final long start, final long end){
        //validatePositions(contig, start, end);
        this.queryName = queryName;
        this.start = start;
        this.end = end;
    }

    @Override
    public String getQueryName() { return queryName; }

    @Override
    public long getStart() { return start; }

    @Override
    public long getEnd() { return end; }

}
