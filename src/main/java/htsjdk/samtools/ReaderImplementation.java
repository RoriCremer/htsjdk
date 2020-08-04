package htsjdk.samtools;

/**
 * Internal interface for SAM/BAM/CRAM file reader implementations,
 * as distinct from non-file-based readers.
 *
 * Implemented as an abstract class to enforce better access control.
 *
 * TODO -- Many of these methods only apply for a subset of implementations,
 * TODO -- and either no-op or throw an exception for the others.
 * TODO -- We should consider refactoring things to avoid this;
 * TODO -- perhaps we can get away with not having this class at all.
 */
public abstract class ReaderImplementation implements PrimitiveSamReader {
    abstract void enableFileSource(final SamReader reader, final boolean enabled);

    abstract void enableIndexCaching(final boolean enabled);

    abstract void enableIndexMemoryMapping(final boolean enabled);

    abstract void enableCrcChecking(final boolean enabled);

    abstract void setSAMRecordFactory(final SAMRecordFactory factory);

    abstract void setValidationStringency(final ValidationStringency validationStringency);
}
