package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

/**
 * An output resource backed by an {@link IOPath}.
 */
public class OutputIOPathResource extends OutputResource {
    private final IOPath ioPath;

    public OutputIOPathResource(final String contentType, final IOPath ioPath) {
        this(contentType, ioPath, null, null);
    }

    public OutputIOPathResource(final String contentType, final IOPath ioPath, final String tag) {
        this(contentType, ioPath, tag, null);
    }

    public OutputIOPathResource(
            final String contentType,
            final IOPath ioPath,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(contentType,
                ValidationUtils.nonNull(ioPath, "A non null output path is required").getRawInputString(),
                tag,
                tagAttributes);
        this.ioPath = ioPath;
    }

    @Override
    public Optional<IOPath> getIOPath() { return Optional.of(ioPath); }

    @Override
    public Optional<OutputStream> getOutputStream() { return Optional.of(ioPath.getOutputStream()); }

}
