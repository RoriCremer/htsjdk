package htsjdk.plugin.bundle;

import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.util.Map;

/**
 * An input resource backed by an {@link htsjdk.samtools.seekablestream.SeekableStream}.
 */
public class InputSeekableStreamResource extends InputStreamResource {

    private final SeekableStream seekableStream;

    public InputSeekableStreamResource(final SeekableStream seekableStream, final String displayName, final String contentType) {
        this(seekableStream, displayName, contentType, null);
    }

    public InputSeekableStreamResource(
            final SeekableStream seekableStream,
            final String displayName,
            final String contentType,
            final String subContentType) {
        this(seekableStream, displayName, contentType, subContentType, null);
    }

    public InputSeekableStreamResource(
            final SeekableStream seekableStream,
            final String displayName,
            final String contentType,
            final String subContentType,
            final String tag) {
        this(seekableStream, displayName, contentType, subContentType, tag, null);
    }

    public InputSeekableStreamResource(
            final SeekableStream seekableStream,
            final String displayName,
            final String contentType,
            final String subContentType,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(seekableStream, displayName, contentType, subContentType, tag, tagAttributes);
        ValidationUtils.nonNull(seekableStream, "A non-null seekable input stream must be provided");
        this.seekableStream = seekableStream;
    }

    public SeekableStream getSeekableInputStream() { return seekableStream; }
}
