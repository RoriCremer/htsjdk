package htsjdk.plugin.interval;

public class ZeroBasedHalfOpen implements HtsInterval {

    private final String queryName;
    private final long start;
    private final long end;

    public ZeroBasedHalfOpen(final String queryName, final long start, final long end){
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
