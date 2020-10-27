package htsjdk.codecs.reads.bam;

import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.interval.HtsInterval;
import htsjdk.plugin.reads.ReadsCodec;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.util.FileExtensions;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class BAMCodec implements ReadsCodec {
    public static HtsCodecVersion BAM_DEFAULT_VERSION = new HtsCodecVersion(1, 0,0);

    private final Set<String> extensionMap = new HashSet(Arrays.asList(FileExtensions.BAM));

    @Override
    public ReadsFormat getFileFormat() { return ReadsFormat.BAM; }

    @Override
    public boolean canDecodeExtension(final IOPath ioPath) {
        return extensionMap.stream().anyMatch(ext-> ioPath.hasExtension(ext));
    }

    @Override
    public boolean canDecodeExtension(final Path path) {
        return extensionMap.stream().anyMatch(ext-> path.endsWith(ext));
    }

    //TODO: implement this
    boolean isQueryable() { return false; }

    //TODO: implement this
    boolean hasIndex() { return false; }

    Iterator<SAMRecord> query(final String queryName, final long start, final long end, final boolean contained) { return null; }

    Iterator<SAMRecord> queryOverlapping(final String queryName, final long start, final long end) { return null; }

    Iterator<SAMRecord> queryContained(final String queryName, final long start, final long end) { return null; }

    Iterator<SAMRecord> query(final List<HtsInterval> intervals, final boolean contained) { return null; }

    Iterator<SAMRecord> queryOverlapping(final List<HtsInterval> intervals) { return null; }

    Iterator<SAMRecord> queryContained(final List<HtsInterval> intervals) { return null; }

    Iterator<SAMRecord> queryStart(final String queryName, final int start) { return null; }

}
