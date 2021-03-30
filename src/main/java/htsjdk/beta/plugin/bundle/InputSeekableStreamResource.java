package htsjdk.beta.plugin.bundle;

import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * An input resource backed by an {@link htsjdk.samtools.seekablestream.SeekableStream}.
 */
public class InputSeekableStreamResource extends InputStreamResource implements Serializable {
    private static final long serialVersionUID = 1L;

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
        ValidationUtils.nonNull(seekableStream, "seekable input stream");
        this.seekableStream = seekableStream;
    }

    public SeekableStream getSeekableInputStream() { return seekableStream; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InputSeekableStreamResource)) return false;
        if (!super.equals(o)) return false;

        InputSeekableStreamResource that = (InputSeekableStreamResource) o;

        return getSeekableStream().equals(that.getSeekableStream());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getSeekableStream().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", super.toString(), seekableStream);
    }
}
