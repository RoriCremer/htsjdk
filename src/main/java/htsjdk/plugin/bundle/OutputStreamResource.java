package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;

import java.io.OutputStream;
import java.util.Optional;

/**
 * An output resource backed by an {@link java.io.OutputStream}.
 */
public class OutputStreamResource extends OutputResource  {
    private final OutputStream outputStream;

    public OutputStreamResource(final String contentType, final String displayName, final OutputStream outputStream) {
        super(contentType, displayName);
        this.outputStream = outputStream;
    }

    @Override
    public Optional<IOPath> getIOPath() { return Optional.empty(); }

    @Override
    public Optional<OutputStream> getOutputStream() {
        //TODO: once this stream has been exhausted, retrieving it will no longer be useful
        return Optional.of(outputStream);
    }
}
