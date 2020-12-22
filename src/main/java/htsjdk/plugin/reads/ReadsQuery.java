package htsjdk.plugin.reads;

import htsjdk.samtools.SAMRecord;

import java.util.Iterator;

/**
 * Reads/alignment-specific query methods (reads-specific extension of HtsQuery)
 */
public interface ReadsQuery {

    // query unmapped Reads
    default Iterator<SAMRecord> queryUnmapped() {
        throw new IllegalStateException("Not implemented");
    };

    // Fetch the mate for the given read.
    default SAMRecord queryMate(SAMRecord rec) {
        throw new IllegalStateException("Not implemented");
    };
}
