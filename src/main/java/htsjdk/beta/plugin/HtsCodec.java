package htsjdk.beta.plugin;

import htsjdk.io.IOPath;
import htsjdk.beta.plugin.bundle.InputBundle;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Base interface implemented by all HtsCodec implementations.
 *
 * A codec is a handler for a specific combination of file-format, version, possibly specific
 * to some URI protocol. For example, a BAM codec handles the BAM format for the "file://" protocol, or other nio.Path
 * file system providers such as gs:// or hdfs://, and an HtsGet codec handles BAM via "http://" or "htsget://" protocol.
 *
 * Codecs may delegate to/reuse shared encoder/decoder implementations.
 *
 * @param <F> an enum representing the various serialized formats handled by this codec
 * @param <D> decoder options for this codec
 * @param <E> encoder options for this codec
 */
// i.e., Htsget codec can't assume it can call toPath on http: paths, .tsv codecs can't tell from extension
// and MUST resolve to a stream
public interface HtsCodec<
        F extends Enum<F>,
        D extends HtsDecoderOptions,
        E extends HtsEncoderOptions>
        extends Upgradeable {

    HtsCodecType getCodecType();

    /**
     * Return a value representing the underlying file format handled by this codec.
     * @return
     */
    F getFileFormat();

    HtsCodecVersion getVersion();

    default String getDisplayName() {
        return String.format("Codec %s for %s version %s", getFileFormat(), getVersion(), getClass().getName());
    }

    // Get the minimum number of bytes this codec requires to determine whether it can decode a stream.
    int getSignatureSize();

    // Return true if this codec claims to handle the URI scheme and file extension for this IOPath.
    boolean canDecodeURI(final IOPath resource);

    // Return true if this codec claims to handle the file extension for this Path.
    boolean canDecodeExtension(final Path path);

    boolean canDecodeSignature(final InputStream inputStream, final String sourceName);

    //TODO: we should get rid of all of these overloads, and just implement one each for decoder
    // and encoder that takes an InputBundle or OutputBundle.

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath);

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath, final D decodeOptions);

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName);

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final InputBundle bundle, final D decodeOptions);

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName, final D decodeOptions);

    HtsEncoder<F, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath);

    HtsEncoder<F, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath, final E encodeOptions);

    HtsEncoder<F, ?, ? extends HtsRecord> getEncoder(final OutputStream os, final String displayName);

    HtsEncoder<F, ?, ?> getEncoder(final OutputStream os, final String displayName, final E encodeOptions);
}
