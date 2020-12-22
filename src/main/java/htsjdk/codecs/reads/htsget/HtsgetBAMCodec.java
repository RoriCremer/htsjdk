package htsjdk.codecs.reads.htsget;

import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.HtsDecoder;
import htsjdk.plugin.HtsEncoder;
import htsjdk.plugin.HtsRecord;
import htsjdk.plugin.reads.ReadsCodec;
import htsjdk.plugin.reads.ReadsEncoderOptions;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.samtools.HtsgetBAMFileReader;
import htsjdk.samtools.util.FileExtensions;
import htsjdk.samtools.util.htsget.HtsgetFormat;
import htsjdk.samtools.util.htsget.HtsgetRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//TODO:
// does this need custom ReaderOptions ?
// should this class (and HtsgetBAMCodec) be repurposed to HtsgetReadsCodec/Decoder?

// An Htsget codec for reading BAM.
// Note: there is no Htsget encoder
public abstract class HtsgetBAMCodec implements ReadsCodec {
    public static HtsCodecVersion HTSGET_VERSION = new HtsCodecVersion(1, 2, 0);
    public static HtsCodecVersion BAM_DEFAULT_VERSION = new HtsCodecVersion(1, 0,0);

    private final Set<String> extensionMap = new HashSet(Arrays.asList(FileExtensions.BAM));

    @Override
    public HtsCodecVersion getVersion() { return HTSGET_VERSION; }

    @Override
    public ReadsFormat getFileFormat() { return ReadsFormat.BAM; }

    @Override
    public int getSignatureSize() {
        return 0;
    }

    @Override
    public boolean canDecodeURI(final IOPath ioPath) {
        final boolean hasExtension = extensionMap.stream().anyMatch(ext-> ioPath.hasExtension(ext));
        final String scheme = ioPath.getScheme();
        final boolean hasScheme =
                scheme.equals(HtsgetBAMFileReader.HTSGET_SCHEME) ||
                        scheme.equals("https") ||
                        scheme.equals("http");

        //TODO: does this check for "/reads/" at the start of the path ? should it ?
        final HtsgetRequest htsgetRequest = new HtsgetRequest(ioPath.getURI());
        // no format == default == BAM
        final boolean matchesRequestType = htsgetRequest.getFormat() == null || htsgetRequest.getFormat() == HtsgetFormat.BAM;

        return hasExtension && hasScheme && matchesRequestType;
    }

    @Override
    public boolean canDecodeExtension(final Path path) {
        return extensionMap.stream().anyMatch(ext-> path.endsWith(ext));
    }

    @Override
    public boolean canDecodeSignature(final InputStream inputStream, final String sourceName) {
        return false;
    }

    @Override
    public HtsDecoder<ReadsFormat, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath) {
        return null;
    }

    //defaults for getEncoder tos return null, since there are no htsget encoders...
    @Override
    public HtsEncoder<ReadsFormat, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath) {
        return null;
    }

    @Override
    public HtsEncoder<ReadsFormat, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath, final ReadsEncoderOptions encodeOptions) {
        return null;
    }

    @Override
    public HtsEncoder<ReadsFormat, ?, ? extends HtsRecord> getEncoder(final OutputStream os, final String displayName) {
        return null;
    }

    @Override
    public HtsEncoder<ReadsFormat, ?, ?> getEncoder(final OutputStream os, final String displayName, final ReadsEncoderOptions encodeOptions) {
        return null;
    }

    boolean isQueryable() { return true; }

    boolean hasIndex() { return false; }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        return false;
    }
}
