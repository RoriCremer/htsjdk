package htsjdk.beta.plugin.bundle;

import htsjdk.beta.plugin.registry.SignatureProbingInputStream;
import htsjdk.io.IOPath;
import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Optional;

/**
 * Base class for {@link BundleResource} implementations. }.
 */
public abstract class BundleResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String displayName;
    private final String contentType;
    private final Optional<String> subContentType;

    /**
     *
     * @param displayName A user-recognizable name for this resource. Used for error messages. May not be null or
     *                    0 length.
     * @param contentType The content type for this resource. Can be any string, but it must be unique for this
     *                    resource. May not be null or zero length.
     * @param subContentType The (optional) sub content type for this resource. Can be any string, i.e, "BAM" for
     *                       a resource with content type "READS". Predefined sub content type strings are defined
     *                       in {@link BundleResourceType}.
     */
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

    /**
     * @return the display name for this resource
     */
    public String getDisplayName() { return displayName; }

    /**
     * @return the content type for this resource
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @return the sub content type for this resource, or Optional.empty if not present
     */
    public Optional<String> getSubContentType() {
        return subContentType;
    }

    /**
     * @return an IOPath for this resource, or Optional.empty if not present
     */
    public Optional<IOPath> getIOPath() { return Optional.empty(); }

    /**
     * @return an {@link InputStream} for this resource, or Optional.empty if {@link
     * BundleResource#isInputResource()} is false for this resource
     */
    public Optional<InputStream> getInputStream() { return Optional.empty(); }

    /**
     * @return an {@link OutputStream} for this resource, or Optional.empty if {@link
     * BundleResource#isOutputResource()} is false for this resource
     */
    public Optional<OutputStream> getOutputStream() { return Optional.empty(); }

    /**
     * Obtain a stream over the first bytes of this stream to support signature probing.
     * @return
     */
    public SignatureProbingInputStream getSignatureProbingStream(final int prefixSize) { return null; }

    /**
     * @return an {@link SeekableStream} for this resource, or Optional.empty if {@link
     * BundleResource#isInputResource()} is false for this resource
     */
    public Optional<SeekableStream> getSeekableStream() { return Optional.empty(); }

    /**
     * @return true if it is safe to call get {@link #getInputStream()} on this resource
     */
    public boolean isInputResource() { return false; }

    /**
     * @return true if {@link BundleResource#isInputResource()} is true for this resource AND the resource
     * is seekable for random access, i.e., it is safe to call get {@link #getSeekableStream()} on this resource.
     */
    public boolean isRandomAccessResource() { return false; }

    /**
     * @return true if it is safe to call get {@link #getOutputStream()} on this resource
     */
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
