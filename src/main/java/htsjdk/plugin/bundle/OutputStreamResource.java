package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

/**
 * An output resource backed by an {@link java.io.OutputStream}.
 */
public class OutputStreamResource extends OutputResource {
    private final OutputStream outputStream;

    public OutputStreamResource(final OutputStream outputStream, final String displayName, final String contentType) {
        this(outputStream, displayName, contentType, null);
    }

    public OutputStreamResource(
            final OutputStream outputStream,
            final String displayName,
            final String contentType,
            final String subContentType) {
        this(outputStream, displayName, contentType, subContentType, null);
    }

    public OutputStreamResource(
            final OutputStream outputStream,
            final String displayName,
            final String contentType,
            final String subContentType,
            final String tag) {
        this(outputStream, displayName, contentType, subContentType, tag, null);
    }

    public OutputStreamResource(
            final OutputStream outputStream,
            final String displayName,
            final String contentType,
            final String subContentType,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(displayName, contentType, subContentType, tag, tagAttributes);
        ValidationUtils.nonNull(outputStream, "output stream");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutputStreamResource)) return false;
        if (!super.equals(o)) return false;

        OutputStreamResource that = (OutputStreamResource) o;

        return getOutputStream().equals(that.getOutputStream());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getOutputStream().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", super.toString(), outputStream);
    }
}
