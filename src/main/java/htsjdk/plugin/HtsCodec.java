package htsjdk.plugin;

import htsjdk.io.IOPath;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Base interface that must be implemented by all htsjdk codecs.
 *
 * A codec class represents a single file format/version
 */
public interface HtsCodec<FORMAT, READER extends HtsReader, WRITER extends HtsWriter> extends Upgradeable {

    HtsCodecType getType();

    HtsCodecVersion getVersion();

    default String getDisplayName() {
        return String.format("Codec for %s version %s", getFormat(), getVersion());
    }

    FORMAT getFormat();

    // Get the minimum number of bytes this codec requires to determine whether it can decode a stream.
    int getSignatureSize();

    boolean canDecodeExtension(final IOPath resource);

    boolean canDecodeExtension(final Path path);

    boolean canDecodeSignature(final byte[] streamSignature);

    READER getReader(final IOPath ioPath);

    READER getReader(final InputStream is, final String displayName);

    WRITER getWriter(final IOPath ioPath);

    WRITER getWriter(final OutputStream os, final String displayName);

}
