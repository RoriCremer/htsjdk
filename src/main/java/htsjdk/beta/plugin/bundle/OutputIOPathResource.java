package htsjdk.beta.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * An output resource backed by an {@link IOPath}.
 */
public class OutputIOPathResource extends OutputResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final IOPath ioPath;

    public OutputIOPathResource(final IOPath ioPath, final String contentType) {
        this(ioPath, contentType,null);
    }

    public OutputIOPathResource(final IOPath ioPath, final String contentType, final String subContentType) {
        super(ValidationUtils.nonNull(ioPath, "output path").getRawInputString(),
                contentType,
                subContentType);
        this.ioPath = ioPath;
    }

    @Override
    public Optional<IOPath> getIOPath() { return Optional.of(ioPath); }

    @Override
    public Optional<OutputStream> getOutputStream() { return Optional.of(ioPath.getOutputStream()); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutputIOPathResource)) return false;
        if (!super.equals(o)) return false;

        OutputIOPathResource that = (OutputIOPathResource) o;

        return ioPath.equals(that.ioPath);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ioPath.hashCode();
        return result;
    }

}
