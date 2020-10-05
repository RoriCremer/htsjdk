package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Base interface that must be implemented by all htsjdk codecs.
 *
 * A codec class represents a single file format/version
 */
public interface HtsCodec<FORMAT, READER, WRITER> extends Upgradeable {

    HtsCodecType getType();

    HtsCodecVersion getVersion();

    String getDisplayName();

    FORMAT getFormat();

    // Get the minimum number of bytes this codec requires to determine whether it can decode a stream.
    int getFileSignatureSize();

    //TODO: contract should say to first look at the IOPath (extension), if acceptable, delegate to the stream
    // canDecode, which in turn delegates to the
    boolean canDecode(final IOPath resource);

    boolean canDecode(final Path path);

    boolean canDecode(final byte[] streamSignature);


    default READER getReader(final IOPath ioPath) {
        return getReader(ioPath.getInputStream(), ioPath.getRawInputString());
    }

    READER getReader(final InputStream is, final String displayName);

    default WRITER getWriter(final IOPath ioPath) {
        return getWriter(ioPath.getOutputStream(), ioPath.getRawInputString());
    }

    WRITER getWriter(final OutputStream os, final String displayName);

}
