package htsjdk.beta.plugin;

import htsjdk.io.IOPath;
import htsjdk.beta.plugin.bundle.Bundle;

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

    /**
     * This value represents the set of interfaces exposed by this codec. The underlying file format
     * may vary amongst different codecs that have the same codec type, since the serialized file format
     * may be different (i.e., both the BAM and HTSGET_BAM codecs have "codec type" ALIGNED_READS because
     * they render records as SAMRecord, but underlying file formats are different.
     * @return
     */
    // EX: ALIGNED_READS
    HtsCodecType getCodecType();

    /**
     * Return a value representing the underlying file format handled by this codec (i.e., for codec type
     * ALIGNED_READS, the values may be from BAM, CRAM, SAM).
     * @return
     */
    // EX: BAM
    F getFileFormat();

    HtsCodecVersion getVersion();

    default String getDisplayName() {
        return String.format("Codec %s for %s version %s", getFileFormat(), getVersion(), getClass().getName());
    }

    // Get the number of bytes this codec requires to determine whether it can decode a stream.
    default int getMinimumStreamDecodeSize() { return getSignatureSize(); }

    // Get the number of bytes this codec requires to determine whether a stream contains the correct signature/version.
    int getSignatureSize();

    //TODO: is this really "claimProtocol" ?
    // Return true if this codec claims to handle the URI protocol scheme (and file extension) for this
    // IOPath. Codecs should only return true if the supported protocol scheme requires special handling that
    // precludes use of an NIO file system provider
    default boolean claimCustomURI(final IOPath resource) { return false; }

    // TODO: all implementations only look at the extension. Protocol scheme is excluded because
    // codecs don't opt-out based on protocol scheme, only opt-in (via claimURI). Perhaps we
    // should rename this, or just use canDecodeExtension, which is currently unused ?
    boolean canDecodeURI(final IOPath resource);

    //TODO: remove this Path overload and replace with IOPath overload ? Or do we need this at all ?
    // Return true if this codec claims to handle the file extension for this Path.
    boolean canDecodeExtension(final Path path);

    // Decode the input stream. Read as many bytes as getMinimumStreamDecodeSize() returns for this codec.
    // Never more than that. Don't close it. Don't mark it. Don't reset it. Ever.
    boolean canDecodeSignature(final InputStream inputStream, final String sourceName);

    //TODO: we should get rid of all of these overloads, and just implement one each for decoder
    // and encoder that takes a Bundle ?

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath);

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath, final D decodeOptions);

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName);

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final Bundle bundle, final D decodeOptions);

    HtsDecoder<F, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName, final D decodeOptions);

    HtsEncoder<F, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath);

    HtsEncoder<F, ?, ? extends HtsRecord> getEncoder(final IOPath outputPath, final E encodeOptions);

    HtsEncoder<F, ?, ? extends HtsRecord> getEncoder(final OutputStream os, final String displayName);

    HtsEncoder<F, ?, ?> getEncoder(final OutputStream os, final String displayName, final E encodeOptions);
}
