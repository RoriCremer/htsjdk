package htsjdk.plugin.reads;

/**
 * Represents the possible underlying serialized formats for reads data.
 */
public enum ReadsFormat {
    SAM,
    BAM,
    CRAM,
    HTSGET_BAM,
    HTSGET_CRAM,
    SRA
}
