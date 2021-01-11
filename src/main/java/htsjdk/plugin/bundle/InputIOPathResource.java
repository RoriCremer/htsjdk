package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * An input resource backed by an {@link IOPath}.
 */
public class InputIOPathResource extends InputResource {
    private final IOPath ioPath;

    public InputIOPathResource(final String contentType, final IOPath ioPath) {
        this(contentType, ioPath, null, null);
    }

    public InputIOPathResource(final String contentType, final IOPath ioPath, final String tag) {
        this(contentType, ioPath, tag, null);
    }

    public InputIOPathResource(
            final String contentType,
            final IOPath ioPath,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(contentType,
                ValidationUtils.nonNull(ioPath, "A non null input path is required").getRawInputString(),
                tag,
                tagAttributes);
        this.ioPath = ioPath;
    }

    @Override
    public Optional<IOPath> getIOPath() { return Optional.of(ioPath); }

    @Override
    public Optional<InputStream> getInputStream() {
        return Optional.of(ioPath.getInputStream());
    }

    @Override
    public Optional<SeekableStream> getSeekableStream() {
        return Optional.empty();
    }
}
