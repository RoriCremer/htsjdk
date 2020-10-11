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
public interface HtsCodec<
        FORMAT,
        ENCODER_OPTIONS,
        ENCODER extends HtsEncoder<?, ?, ?>,
        DECODER_OPTIONS,
        DECODER extends HtsDecoder<?, ?, ?>> extends Upgradeable {

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

    // Get a codec that matches this ioPath(Select first by extension, then stream signature)
    DECODER getDecoder(final IOPath inputPath);

    DECODER getDecoder(final IOPath inputPath, final DECODER_OPTIONS readOptions);

    DECODER getDecoder(final InputStream is, final String displayName);

    DECODER getDecoder(final InputStream is, final String displayName, final DECODER_OPTIONS readOptions);

    ENCODER getEncoder(final IOPath outputPath);

    ENCODER getEncoder(final IOPath outputPath, final ENCODER_OPTIONS writeOptions);

    ENCODER getEncoder(final OutputStream os, final String displayName);

    ENCODER getEncoder(final OutputStream os, final String displayName, final ENCODER_OPTIONS writeOptions);
}
