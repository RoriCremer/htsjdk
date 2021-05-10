package htsjdk.beta.plugin.bundle;

import htsjdk.beta.plugin.registry.SignatureProbingInputStream;
import htsjdk.samtools.util.RuntimeIOException;
import htsjdk.utils.ValidationUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

//TODO: if getInputStream has side effects then we need to change toString/hashCode etc.

/**
 * An input resource backed by an {@link java.io.InputStream}.
 */
public class InputStreamResource extends BundleResource {
    private final InputStream rawInputStream;   // the stream as provided by the caller
    private BufferedInputStream inputStream;    // buffered wrapper to allow for signature probing
    private int signaturePrefixSize = -1;
    private byte[] signaturePrefix;

    /**
     * @param inputStream The {@link InputStream} to use for this resource. May not be null.
     * @param displayName The display name for this resource. May not be null or 0-length.
     * @param contentType The content type for this resource. May not be null or 0-length.
     */
    public InputStreamResource(final InputStream inputStream, final String displayName, final String contentType) {
        this(inputStream, displayName, contentType, null);
    }

    /**
     * @param inputStream The {@link InputStream} to use for this resource. May not be null.
     * @param displayName The display name for this resource. May not be null or 0-length.
     * @param contentType The content type for this resource. May not be null or 0-length.
     * @param subContentType The sub content type for this resource. May not be null or 0-length.
     */
    public InputStreamResource(
            final InputStream inputStream,
            final String displayName,
            final String contentType,
            final String subContentType) {
        super(displayName, contentType, subContentType);
        ValidationUtils.nonNull(inputStream, "input stream");

        // wrap the input stream in a SignatureProbingStream to support the codec resolution
        // process, which needs to be able to mark/read/reset the stream several times to
        // sniff the content type and version
        this.rawInputStream = inputStream;
    }

    @Override
    public Optional<InputStream> getInputStream() {
        return Optional.of(inputStream == null ? rawInputStream : inputStream);
    }

    @Override
    public SignatureProbingInputStream getSignatureProbingStream(final int requestedPrefixSize) {
        if (signaturePrefix == null) {
            signaturePrefix = new byte[requestedPrefixSize];
            try {
                // we don't want this code to close the underlying stream yet so don't use try-with-resources
                inputStream = new BufferedInputStream(rawInputStream, requestedPrefixSize);
                inputStream.mark(requestedPrefixSize);
                inputStream.read(signaturePrefix);
                inputStream.reset();
                this.signaturePrefixSize = requestedPrefixSize;
            } catch (final IOException e) {
                throw new RuntimeIOException(
                        String.format("Error during signature probing with prefix size %d", requestedPrefixSize),
                        e);
            }
        } else if (requestedPrefixSize > signaturePrefixSize) {
            throw new IllegalArgumentException(
                    String.format("A signature probing size of %d was requested, but a probe size of %d has already been established",
                    requestedPrefixSize, signaturePrefixSize));
        }
        return new SignatureProbingInputStream(signaturePrefix, signaturePrefixSize);
    }

    @Override
    public boolean isInputResource() { return true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InputStreamResource)) return false;
        if (!super.equals(o)) return false;

        InputStreamResource that = (InputStreamResource) o;

        return getInputStream().equals(that.getInputStream());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getInputStream().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", super.toString(), rawInputStream);
    }
}
