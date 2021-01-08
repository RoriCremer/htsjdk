package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.Optional;

/**
 * Base class for {@link InputResource} or {@link OutputResource}.
 */
public abstract class BundleResource {
    private final String displayName;
    private final String contentType;

    public BundleResource(final String contentType, final String displayName) {
        ValidationUtils.nonNull(contentType, "A content type must be provided");
        ValidationUtils.nonNull(displayName, "A display name must be provided");
        this.contentType = contentType;
        this.displayName = displayName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDisplayName() { return displayName; }

    public Optional<IOPath> getIOPath() { return Optional.empty(); };   // may be None for some sub-types

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BundleResource that = (BundleResource) o;

        if (!getDisplayName().equals(that.getDisplayName())) return false;
        return getContentType().equals(that.getContentType());
    }

    @Override
    public int hashCode() {
        int result = getDisplayName().hashCode();
        result = 31 * result + getContentType().hashCode();
        return result;
    }

}
