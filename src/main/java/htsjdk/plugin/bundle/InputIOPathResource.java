package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.samtools.seekablestream.SeekableStream;

import java.io.InputStream;
import java.util.Optional;

/**
 * An input resource backed by an {@link IOPath}.
 */
public class InputIOPathResource extends InputResource {
    private final IOPath ioPath;

    public InputIOPathResource(final String contentType, final IOPath ioPath) {
        super(contentType, ioPath.getRawInputString());
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
        //TODO: implement this!
        return Optional.empty();
    }
}
