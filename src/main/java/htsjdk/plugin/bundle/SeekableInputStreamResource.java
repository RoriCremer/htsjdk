package htsjdk.plugin.bundle;

import htsjdk.samtools.seekablestream.SeekableStream;

/**
 * An input resource backed by an {@link htsjdk.samtools.seekablestream.SeekableStream}.
 */
public class SeekableInputStreamResource extends InputStreamResource {

    private final SeekableStream seekableStream;

    public SeekableInputStreamResource(final String contentType, final String displayName, final SeekableStream seekableStream) {
        super(contentType, displayName, seekableStream);
        this.seekableStream = seekableStream;
    }

    public SeekableStream getSeekableInputStream() { return seekableStream; }
}
