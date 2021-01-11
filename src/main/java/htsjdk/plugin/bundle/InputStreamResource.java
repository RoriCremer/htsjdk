package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.util.Optional;

/**
 * An input resource backed by an {@link java.io.InputStream}.
 */
public class InputStreamResource extends InputResource {

    private final InputStream inputStream;

    public InputStreamResource(final String contentType, final String displayName, final InputStream inputStream) {
        super(ValidationUtils.nonNull(contentType), ValidationUtils.nonNull(displayName), null,null);
        ValidationUtils.nonNull(inputStream, "A non-null input stream must be provided");
        this.inputStream = inputStream;
    }

    @Override
    public Optional<IOPath> getIOPath() { return Optional.empty(); }

    public Optional<InputStream> getInputStream() { return Optional.of(inputStream); }

    @Override
    public Optional<SeekableStream> getSeekableStream() {
        return Optional.empty();
    }
}
