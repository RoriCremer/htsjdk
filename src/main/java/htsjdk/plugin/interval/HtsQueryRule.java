package htsjdk.plugin.interval;

public enum HtsQueryRule {
    OVERLAPPING,
    CONTAINED;

    // compatibility with existing code that uses a boolean
    public boolean toContained() { return this == CONTAINED ? true : false; }
}
