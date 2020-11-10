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
        FILE_FORMAT,
        BUNDLE_TYPE,
        DECODER_OPTIONS extends HtsDecoderOptions,
        ENCODER_OPTIONS extends HtsEncoderOptions>
        extends Upgradeable {

    HtsCodecType getType();

    HtsCodecVersion getVersion();

    default String getDisplayName() {
        return String.format("Codec %s for %s version %s", getFileFormat(), getVersion(), getClass().getName());
    }

    FILE_FORMAT getFileFormat();

    // Get the minimum number of bytes this codec requires to determine whether it can decode a stream.
    int getSignatureSize();

    boolean canDecodeExtension(final IOPath resource);

    boolean canDecodeExtension(final Path path);

    boolean canDecodeSignature(final InputStream inputStream, final String sourceName);

    // Get a codec that matches this ioPath(Select first by extension, then stream signature)
    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath);

    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath, final DECODER_OPTIONS decodeOptions);

    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName);

    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final BUNDLE_TYPE bundle, final DECODER_OPTIONS decodeOptions);

    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName, final DECODER_OPTIONS decodeOptions);

    HtsEncoder<FILE_FORMAT, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath);

    HtsEncoder<FILE_FORMAT, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath, final ENCODER_OPTIONS encodeOptions);

    HtsEncoder<FILE_FORMAT, ?, ? extends HtsRecord> getEncoder(final OutputStream os, final String displayName);

    HtsEncoder<FILE_FORMAT, ?, ?> getEncoder(final OutputStream os, final String displayName, final ENCODER_OPTIONS encodeOptions);
}
