package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * An input resource backed by an {@link IOPath}.
 */
public class InputIOPathResource extends InputResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final IOPath ioPath;

    public InputIOPathResource(final IOPath ioPath, final String contentType) {
        this(ioPath, contentType,null);
    }

    public InputIOPathResource(final IOPath ioPath, final String contentType, final String subContentType) {
        this(ioPath, contentType, subContentType, null);
    }

    public InputIOPathResource(final IOPath ioPath, final String contentType, final String subContentType, final String tag) {
        this(ioPath,  contentType, subContentType, tag, null);
    }

    public InputIOPathResource(
            final IOPath ioPath,
            final String contentType,
            final String subContentType,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(ValidationUtils.nonNull(ioPath, "ioPath").getRawInputString(),
                contentType,
                subContentType,
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

    //TODO: this should generate a seekable stream for underlying file/path, and override isSeekable to match
    @Override
    public Optional<SeekableStream> getSeekableStream() {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InputIOPathResource that = (InputIOPathResource) o;

        return ioPath.equals(that.ioPath);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ioPath.hashCode();
        return result;
    }

}
