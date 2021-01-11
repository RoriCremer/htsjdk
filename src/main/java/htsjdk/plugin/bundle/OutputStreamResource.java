package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

/**
 * An output resource backed by an {@link java.io.OutputStream}.
 */
public class OutputStreamResource extends OutputResource  {
    private final OutputStream outputStream;

    public OutputStreamResource(final String contentType, final String displayName, final OutputStream outputStream) {
        this(contentType, displayName, outputStream, null, null);
    }

    public OutputStreamResource(
            final String contentType,
            final String displayName,
            final OutputStream outputStream,
            final String tag) {
        this(contentType, displayName, outputStream, tag, null);
    }

    public OutputStreamResource(
            final String contentType,
            final String displayName,
            final OutputStream outputStream,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(contentType, displayName, tag, tagAttributes);
        ValidationUtils.nonNull(outputStream, "A non null output stream is required");
        this.outputStream = outputStream;
    }

    @Override
    public Optional<IOPath> getIOPath() { return Optional.empty(); }

    /**
     * once this stream has been consumed, retrieving it will no longer be useful...
     * @return
     */
    @Override
    public Optional<OutputStream> getOutputStream() {
        return Optional.of(outputStream);
    }
}
