package htsjdk.plugin.bundle;

import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * An input resource backed by an {@link java.io.InputStream}.
 */
public class InputStreamResource extends InputResource {

    private final InputStream inputStream;

    public InputStreamResource(final InputStream inputStream, final String displayName, final String contentType) {
        this(inputStream, displayName, contentType, null);
    }

    public InputStreamResource(
            final InputStream inputStream,
            final String displayName,
            final String contentType,
            final String subContentType) {
        this(inputStream, displayName, contentType, subContentType, null);
    }

    public InputStreamResource(
            final InputStream inputStream,
            final String displayName,
            final String contentType,
            final String subContentType,
            final String tag) {
        this(inputStream, displayName, contentType, subContentType, tag, null);
    }

    public InputStreamResource(
            final InputStream inputStream,
            final String displayName,
            final String contentType,
            final String subContentType,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(displayName, contentType, subContentType, tag, tagAttributes);
        ValidationUtils.nonNull(inputStream, "A non-null input stream must be provided");
        this.inputStream = inputStream;
    }

    public Optional<InputStream> getInputStream() { return Optional.of(inputStream); }

    @Override
    public Optional<SeekableStream> getSeekableStream() {
        return Optional.empty();
    }
}
