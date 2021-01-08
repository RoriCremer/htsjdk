package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.io.OutputStream;
import java.util.Optional;

/**
 * An output resource backed by an {@link java.io.OutputStream}.
 */
public class OutputStreamResource extends OutputResource  {
    private final OutputStream outputStream;

    public OutputStreamResource(final String contentType, final String displayName, final OutputStream outputStream) {
        super(ValidationUtils.nonNull(contentType, "A content type must be provided"),
              ValidationUtils.nonNull(displayName, "A display name must be provided"));
        ValidationUtils.nonNull(outputStream, "A non-null  output stream must bep rovided");
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
