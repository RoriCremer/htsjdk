package htsjdk.beta.codecs.reads.bam.bamV1_0;

import htsjdk.beta.codecs.reads.bam.BAMDecoder;
import htsjdk.beta.plugin.bundle.Bundle;
import htsjdk.beta.plugin.bundle.BundleResource;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.beta.plugin.HtsCodecVersion;
import htsjdk.beta.plugin.HtsDecoderOptions;
import htsjdk.beta.plugin.bundle.BundleResourceType;
import htsjdk.beta.plugin.interval.HtsInterval;
import htsjdk.beta.plugin.interval.HtsQueryRule;

import htsjdk.beta.plugin.reads.ReadsDecoderOptions;
import htsjdk.samtools.BAMFileReader;
import htsjdk.samtools.DefaultSAMRecordFactory;
import htsjdk.samtools.PrimitiveSamReaderToSamReaderAdapter;
import htsjdk.samtools.QueryInterval;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.util.RuntimeIOException;
import htsjdk.samtools.util.zip.InflaterFactory;
import htsjdk.utils.ValidationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

//TODO: need to guard against multiple iterators

public class BAMDecoderV1_0 extends BAMDecoder {
    private final SamReader samReader;
    private final SAMFileHeader samFileHeader;

    public BAMDecoderV1_0(final IOPath inputPath) {
        this(inputPath, new ReadsDecoderOptions());
    }

    public BAMDecoderV1_0(final IOPath inputPath, final HtsDecoderOptions decoderOptions) {
        super(inputPath);
        ValidationUtils.nonNull(decoderOptions);

        samReader = getSamReader(decoderOptions);
        samFileHeader = samReader.getFileHeader();
    }

    public BAMDecoderV1_0(final Bundle inputBundle, final ReadsDecoderOptions decoderOptions) {
        super(inputBundle, decoderOptions);
        ValidationUtils.nonNull(decoderOptions);
        samReader = getSamReader(decoderOptions);
        samFileHeader = samReader.getFileHeader();
    }

    public BAMDecoderV1_0(final InputStream is, final String displayName) {
        this(is, displayName, new ReadsDecoderOptions());
    }

    public BAMDecoderV1_0(final InputStream is, final String displayName, final HtsDecoderOptions decoderOptions) {
        super(is, displayName);
        ValidationUtils.nonNull(decoderOptions);
        samReader = getSamReader(decoderOptions);
        samFileHeader = samReader.getFileHeader();
    }

    @Override
    public HtsCodecVersion getVersion() {
        return BAMCodecV1_0.VERSION_1;
    }

    @Override
    public SAMFileHeader getHeader() {
        return samFileHeader;
    }

    // HtsQuery methods

    @Override
    public Iterator<SAMRecord> iterator() {
        return samReader.iterator();
    }

    @Override
    public boolean isQueryable() {
        return samReader.isQueryable();
    }

    @Override
    public boolean hasIndex() {
        return samReader.hasIndex();
    }

    @Override
    public Iterator<SAMRecord> query(final List<HtsInterval> intervals, final HtsQueryRule queryRule) {
        final QueryInterval[] queryIntervals = HtsInterval.toQueryIntervalArray(
                intervals,
                samFileHeader.getSequenceDictionary());
        return samReader.query(queryIntervals, queryRule == HtsQueryRule.CONTAINED);
    }

    @Override
    public Iterator<SAMRecord> queryStart(final String queryName, final long start) {
        return samReader.queryAlignmentStart(queryName, HtsInterval.toIntegerSafe(start));
    }

    // ReadsQuery interface methods

    @Override
    public Iterator<SAMRecord> queryUnmapped() {
        return samReader.queryUnmapped();
    }

    @Override
    public SAMRecord queryMate(final SAMRecord rec) {
        return samReader.queryMate(rec);
    }

    @Override
    public void close() {
        try {
            samReader.close();
        } catch (IOException e) {
            throw new HtsjdkIOException(String.format("Exception closing input stream %s for", inputPath), e);
        }
    }

    private SamReader getSamReader(final HtsDecoderOptions decoderOptions) {
        final ReadsDecoderOptions readsDecoderOptions = (ReadsDecoderOptions) decoderOptions;

        SamReader reader;
        if (is != null) {
            //TODO: SamReaderFactory doesn't expose getters for all options (currently most are not exposed),
            // so this is currently not fully honoring the SAMFileWriterFactory

            //TODO: this BAMFileReader stream constructor required changing the member access to protected...
            try {
                final BAMFileReader bamReader = new BAMFileReader(is,
                        null,
                        false,
                        false,
                        readsDecoderOptions.getSamReaderFactory().validationStringency(),
                        new DefaultSAMRecordFactory(),
                        new InflaterFactory());
                return new PrimitiveSamReaderToSamReaderAdapter(bamReader, SamInputResource.of(is));
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        } else if (inputPath != null) {
            reader = readsDecoderOptions.getSamReaderFactory().open(SamInputResource.of(inputPath.toPath()));
        } else {
            // use the bundle
            final Optional<BundleResource> readsInput = inputBundle.get(BundleResourceType.READS);
            if (!readsInput.isPresent()) {
                throw new IllegalArgumentException("No source of reads was provided");
            }
            final Optional<IOPath> readsPath = readsInput.get().getIOPath();
            if (!readsPath.isPresent()) {
                throw new IllegalArgumentException("Currently onlyIOPaths are supported for reads input bundles");
            }
            final SamInputResource readsResource = SamInputResource.of(readsPath.get().toPath());

            final Optional<BundleResource> indexInput = inputBundle.get(BundleResourceType.READS_INDEX);
            if (indexInput.isPresent()) {
                final Optional<IOPath> indexPath = indexInput.get().getIOPath();
                if (!indexPath.isPresent()) {
                    throw new IllegalArgumentException("Currently only IOPaths are supported for index input bundles");
                }
                readsResource.index(indexPath.get().toPath());
            }
            reader = readsDecoderOptions.getSamReaderFactory().open(readsResource);
        }
        return reader;
    }

}
