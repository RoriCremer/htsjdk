package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.plugin.bundle.InputBundle;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Base interface implemented by all codecs.
 *
 * A codec  is a handler for a specific combination of file-format, version, possibly specific
 * to some URI protocol. i.e.:
 *
 *  the BAM codec handles the BAM format for the "file://" protocol; the
 *  the HtsGet codec handles BAM via "http://" or "htsget://" protocol.
 *
 * Codecs may delegate to/reuse shared encoder/decoder implementations.
 *
 * @param <FILE_FORMAT>
 * @param <DECODER_OPTIONS>
 * @param <ENCODER_OPTIONS>
 */
// i.e., Htsget codec can't assume it can call toPath on http: paths, .tsv codecs can't tell from extension
// and MUST resolve to a stream
public interface HtsCodec<
        FILE_FORMAT extends Enum<FILE_FORMAT>,
        DECODER_OPTIONS extends HtsDecoderOptions,
        ENCODER_OPTIONS extends HtsEncoderOptions>
        extends Upgradeable {

    HtsCodecCategory getCodecCategory();

    HtsCodecVersion getVersion();

    default String getDisplayName() {
        return String.format("Codec %s for %s version %s", getFileFormat(), getVersion(), getClass().getName());
    }

    FILE_FORMAT getFileFormat();

    // Get the minimum number of bytes this codec requires to determine whether it can decode a stream.
    int getSignatureSize();

    // Return true if this codec claims to handle the URI scheme and file extension for this IOPath.
    boolean canDecodeURI(final IOPath resource);

    // Return true if this codec claims to handle the file extension for this Path.
    boolean canDecodeExtension(final Path path);

    boolean canDecodeSignature(final InputStream inputStream, final String sourceName);

    //TODO: we should get rid of all of these overloads, and just implement one each for decoder
    // and encoder that takes an InputBundle or OutputBundle.

    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath);

    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath, final DECODER_OPTIONS decodeOptions);

    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName);

    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final InputBundle bundle, final DECODER_OPTIONS decodeOptions);

    HtsDecoder<FILE_FORMAT, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName, final DECODER_OPTIONS decodeOptions);

    HtsEncoder<FILE_FORMAT, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath);

    HtsEncoder<FILE_FORMAT, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath, final ENCODER_OPTIONS encodeOptions);

    HtsEncoder<FILE_FORMAT, ?, ? extends HtsRecord> getEncoder(final OutputStream os, final String displayName);

    HtsEncoder<FILE_FORMAT, ?, ?> getEncoder(final OutputStream os, final String displayName, final ENCODER_OPTIONS encodeOptions);
}
