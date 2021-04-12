package htsjdk.beta.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Optional;

/**
 * Base class for various BundleResource implementations. }.
 */
public abstract class BundleResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String displayName;
    private final String contentType;
    private final Optional<String> subContentType;

    public BundleResource(
            final String displayName,
            final String contentType,
            final String subContentType) {
        ValidationUtils.nonNull(displayName, "display name");
        ValidationUtils.nonNull(contentType, "content type");
        this.displayName = displayName;
        this.contentType = contentType;
        this.subContentType = Optional.ofNullable(subContentType);
    }

    public String getDisplayName() { return displayName; }

    public String getContentType() {
        return contentType;
    }

    public Optional<String> getSubContentType() {
        return subContentType;
    }

    public Optional<IOPath> getIOPath() { return Optional.empty(); }

    public Optional<InputStream> getInputStream() { return Optional.empty(); }

    public Optional<OutputStream> getOutputStream() { return Optional.empty(); }

    public Optional<SeekableStream> getSeekableStream() { return Optional.empty(); }

    public boolean isInputResource() { return false; }

    public boolean isOutputResource() { return false; }

    @Override
    public String toString() {
        return String.format(
                "%s (%s): %s/%s",
                getClass().getSimpleName(),
                getDisplayName(),
                getContentType(),
                getSubContentType().orElse("NONE"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BundleResource that = (BundleResource) o;

        if (!displayName.equals(that.displayName)) return false;
        if (!contentType.equals(that.contentType)) return false;
        return subContentType.equals(that.subContentType);
    }

    @Override
    public int hashCode() {
        int result = displayName.hashCode();
        result = 31 * result + contentType.hashCode();
        result = 31 * result + subContentType.hashCode();
        return result;
    }
}
