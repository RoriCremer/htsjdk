package htsjdk.codecs.bam.bamV1;

import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodec;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;
import htsjdk.codecs.bam.BAMCodecDescriptor;
import htsjdk.samtools.util.FileExtensions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * BAM codec descriptor.
 */
public class BAMV1CodecDescriptor extends BAMCodecDescriptor {

    protected static final String BAM_FILE_EXTENSION = FileExtensions.BAM;
    protected static final String BAM_MAGIC = "BAM\1";
    protected static final HtsCodecVersion VERSION_1 = new HtsCodecVersion(1, 0, 0);

    @Override
    public ReadsFormat getFormat() { return ReadsFormat.BAM; }

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_1;
    }

    @Override
    public String getDisplayName() {
        return String.format("Codec descriptor for BAM version %s", getVersion());
    }

    @Override
    public int getFileSignatureSize() {
        return BAM_MAGIC.length();
    }

    @Override
    public boolean canDecode(final IOPath resource) {
        return resource.hasExtension(BAM_FILE_EXTENSION);
    }

    @Override
    public boolean canDecode(final Path path) {
        return path.endsWith(BAM_FILE_EXTENSION);
    }

    // uses a byte array rather than a stream to reduce the need to repeatedly mark/reset the
    // stream for each codec
    @Override
    public boolean canDecode(final byte[] signatureBytes) {
        return signatureBytes.equals("BAM");
    }

    @Override
    public HtsCodec<ReadsReader, ReadsWriter> getCodec() {
        return new BAMV1Codec();
    }

    @Override
    public ReadsReader getCodecReader(final IOPath ioResource) {
        return getCodecReader(ioResource.toPath());
    }

    @Override
    public ReadsReader getCodecReader(final Path resourcePath) {
        try {
            return getCodecReader(Files.newInputStream(resourcePath), resourcePath.toString());
        } catch (IOException e) {
            throw new HtsjdkIOException(e);
        }
    }

    @Override
    public ReadsReader getCodecReader(final InputStream is, final String displayName) {
        return new BAMV1Reader(is, displayName);
    }

    @Override
    public ReadsWriter getCodecWriter(final IOPath outputPath) {
        return new BAMV1Writer(outputPath.getOutputStream(), outputPath.getRawInputString());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
