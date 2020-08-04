package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base interface that must be implemented by all htsjdk codecs.
 *
 * A codec class represents a single file format/version
 */
public interface HtsCodec<READER, WRITER> extends Upgradeable {

    HtsCodecVersion getVersion();

    default READER getReader(final IOPath ioPath) {
        return getReader(ioPath.getInputStream(), ioPath.getRawInputString());
    }

    READER getReader(final InputStream is, final String displayName);

    default WRITER getWriter(final IOPath ioPath) {
        return getWriter(ioPath.getOutputStream(), ioPath.getRawInputString());
    }

    WRITER getWriter(final OutputStream os, final String displayName);

}
