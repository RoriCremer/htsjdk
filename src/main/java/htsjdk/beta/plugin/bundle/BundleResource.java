package htsjdk.beta.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base class for {@link InputResource} or {@link OutputResource}.
 */
public abstract class BundleResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String displayName;
    private final String contentType;
    private final Optional<String> subContentType;
    private final Optional<String> tag;
    private final Optional<Map<String, String>> tagAttributes;

    public BundleResource(
            final String displayName,
            final String contentType,
            final String subContentType,
            final String tag,
            final Map<String, String> tagAttributes) {
        ValidationUtils.nonNull(displayName, "display name");
        ValidationUtils.nonNull(contentType, "content type");
        this.displayName = displayName;
        this.contentType = contentType;
        this.subContentType = Optional.ofNullable(subContentType);
        this.tag = Optional.ofNullable(tag);
        this.tagAttributes = Optional.ofNullable(tagAttributes);
    }

    public String getDisplayName() { return displayName; }

    public String getContentType() {
        return contentType;
    }

    public Optional<String> getSubContentType() {
        return subContentType;
    }

    public Optional<IOPath> getIOPath() { return Optional.empty(); };   // may be None for some sub-types

    /**
     * Retrieve the tag name for this instance.
     * @return String representing the tagName. May be null if no tag name was set.
     */
    public Optional<String> getTag() { return tag; };

    /**
     * Get the attribute/value pair Map for this instance. May be empty.
     * @return Map of attribute/value pairs for this instance. May be empty if no tags are present. May not be null.
     */
    public Optional<Map<String, String>> getTagAttributes() { return tagAttributes; }

    @Override
    public String toString() {
        final List<String> tagAndAttributeStrings = new ArrayList<>();
        if (getTag().isPresent()) {
            tagAndAttributeStrings.add(getTag().get());
        }
        if (getTagAttributes().isPresent()) {
            getTagAttributes().get().entrySet().stream()
                    .map((e) -> String.format("%s:%s", e.getKey(), e.getValue()))
                    .forEach(s -> tagAndAttributeStrings.add(s));
        }
        return String.format(
                "%s: %s/%s%s",
                getClass().getSimpleName(),
                getContentType(),
                getSubContentType().orElse("NONE"),
                tagAndAttributeStrings.isEmpty() ?
                        "" :
                        " " + tagAndAttributeStrings.stream().collect(Collectors.joining(" ")));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BundleResource that = (BundleResource) o;

        if (!displayName.equals(that.displayName)) return false;
        if (!contentType.equals(that.contentType)) return false;
        if (!subContentType.equals(that.subContentType)) return false;
        if (!tag.equals(that.tag)) return false;
        return tagAttributes.equals(that.tagAttributes);
    }

    @Override
    public int hashCode() {
        int result = displayName.hashCode();
        result = 31 * result + contentType.hashCode();
        result = 31 * result + subContentType.hashCode();
        result = 31 * result + tag.hashCode();
        result = 31 * result + tagAttributes.hashCode();
        return result;
    }
}
