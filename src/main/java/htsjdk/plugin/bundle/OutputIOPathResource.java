package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.io.OutputStream;
import java.util.Optional;

/**
 * An output resource backed by an {@link IOPath}.
 */
public class OutputIOPathResource extends OutputResource {
    private final IOPath ioPath;

    public OutputIOPathResource(final String contentType, final IOPath ioPath) {
        super(contentType, ValidationUtils.nonNull(ioPath, "A non-null iopath must be provided").getRawInputString());
        this.ioPath = ioPath;
    }

    @Override
    public Optional<IOPath> getIOPath() { return Optional.of(ioPath); }

    @Override
    public Optional<OutputStream> getOutputStream() { return Optional.of(ioPath.getOutputStream()); }

}
