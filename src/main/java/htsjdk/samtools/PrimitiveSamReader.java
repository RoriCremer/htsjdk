package htsjdk.samtools;

import htsjdk.samtools.util.CloseableIterator;

/**
 * The minimal subset of functionality needed for a {@link SAMRecord} data source.
 * {@link SamReader} itself is somewhat large and bulky, but the core functionality can be captured in
 * relatively few methods, which are included here. For documentation, see the corresponding methods
 * in {@link SamReader}.
 *
 * See also: {@link PrimitiveSamReaderToSamReaderAdapter}, {@link ReaderImplementation}
 *
 */
public interface PrimitiveSamReader {
    SamReader.Type type();

    default boolean isQueryable() {
        return this.hasIndex();
    }

    boolean hasIndex();

    BAMIndex getIndex();

    SAMFileHeader getFileHeader();

    CloseableIterator<SAMRecord> getIterator();

    CloseableIterator<SAMRecord> getIterator(SAMFileSpan fileSpan);

    SAMFileSpan getFilePointerSpanningReads();

    CloseableIterator<SAMRecord> query(QueryInterval[] intervals, boolean contained);

    CloseableIterator<SAMRecord> queryAlignmentStart(String sequence, int start);

    CloseableIterator<SAMRecord> queryUnmapped();

    void close();

    ValidationStringency getValidationStringency();
}
