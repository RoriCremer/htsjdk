package htsjdk.plugin;

import htsjdk.io.IOPath;

import java.io.InputStream;
import java.nio.file.Path;

// TODO: Descriptor handles:
//  how to find companions
//  about versions, version upgrades
//  knows Types ? (HEADER, RECORD)
// TODO: a codec is versioned; that is, a codec has a read and write implementation for a single file format version,
//    i.e. VCF 4.3, CRAM 3.0, or BAM 1.0

/**
 * Base interface that must be implemented by all htsjdk codec descriptors. Descriptors
 * are lightweight object that are cached by the registry service and used to locate
 * and instantiate a codec for a given input, find siblings, run upgrades.
 */
public interface HtsCodecDescriptor<FORMAT, READER, WRITER> {

    HtsCodecType getType();

    HtsCodecVersion getVersion();

    String getDisplayName();

    FORMAT getFormat();

    HtsCodec<READER, WRITER> getCodec();

    // Get the minimum number of bytes this codec requires to determine whether it can decode a stream.
    int getFileSignatureSize();

    //TODO: contract should say to first look at the IOPath (extension), if acceptable, delegate to the stream
    // canDecode, which in turn delegates to the
    boolean canDecode(final IOPath resource);

    boolean canDecode(final Path path);

    boolean canDecode(final byte[] streamSignature);

    // return the reader/writer for this descriptor's specified format/version

    READER getCodecReader(final IOPath resource);

    READER getCodecReader(final Path path);

    READER getCodecReader(final InputStream is, final String displayName);

    WRITER getCodecWriter(final IOPath ioPath);

}
