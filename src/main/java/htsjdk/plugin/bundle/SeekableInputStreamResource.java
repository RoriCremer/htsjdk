package htsjdk.plugin.bundle;

import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

/**
 * An input resource backed by an {@link htsjdk.samtools.seekablestream.SeekableStream}.
 */
public class SeekableInputStreamResource extends InputStreamResource {

    private final SeekableStream seekableStream;

    public SeekableInputStreamResource(
            final String contentType,
            final String displayName,
            final SeekableStream seekableStream) {
        super(ValidationUtils.nonNull(contentType, "A non-null content type must be provided"),
              ValidationUtils.nonNull(displayName, "A non-null display name must be provided"),
              ValidationUtils.nonNull(seekableStream, "A non-null seekable streammust be provided"));
        this.seekableStream = seekableStream;
    }

    public SeekableStream getSeekableInputStream() { return seekableStream; }
}
