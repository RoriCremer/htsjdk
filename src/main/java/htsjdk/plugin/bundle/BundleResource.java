package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.Map;
import java.util.Optional;

/**
 * Base class for {@link InputResource} or {@link OutputResource}.
 */
public abstract class BundleResource {
    private final String displayName;
    private final String contentType;
    private final String tag;
    private final Map<String, String> tagAttributes;

    public BundleResource(
            final String contentType,
            final String displayName,
            final String tag,
            final Map<String, String> tagAttributes) {
        ValidationUtils.nonNull(contentType, "A content type must be provided");
        ValidationUtils.nonNull(displayName, "A display name must be provided");
        this.contentType = contentType;
        this.displayName = displayName;
        this.tag = tag;
        this.tagAttributes = tagAttributes;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDisplayName() { return displayName; }

    public Optional<IOPath> getIOPath() { return Optional.empty(); };   // may be None for some sub-types

    /**
     * Retrieve the tag name for this instance.
     * @return String representing the tagName. May be null if no tag name was set.
     */
    public Optional<String> getTag() { return Optional.ofNullable(tag); };

    /**
     * Get the attribute/value pair Map for this instance. May be empty.
     * @return Map of attribute/value pairs for this instance. May be empty if no tags are present. May not be null.
     */
    public Optional<Map<String, String>> getTagAttributes() { return Optional.ofNullable(tagAttributes); }

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
