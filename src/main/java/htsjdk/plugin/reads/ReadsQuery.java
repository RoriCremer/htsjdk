package htsjdk.plugin.reads;

import htsjdk.samtools.SAMRecord;

import java.util.Iterator;

/**
 * Reads/alignment-specific query methods (reads-specific extension of HtsQuery)
 */
public interface ReadsQuery {

    // TODO: remove the default implementations once all the reads codecs implement these

    default Iterator<SAMRecord> queryUnmapped() {
        throw new IllegalStateException("Not implemented");
    };

    // TODO: do we need to retain this ?
    // Fetch the mate for the given read.
    default SAMRecord queryMate(SAMRecord rec) {
        throw new IllegalStateException("Not implemented");
    };
}
