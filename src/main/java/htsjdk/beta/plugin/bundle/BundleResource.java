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

    public Optional<IOPath> getIOPath() { return Optional.empty(); };   // may be None for some sub-types

    @Override
    public String toString() {
        return String.format(
                "%s: %s/%s",
                getClass().getSimpleName(),
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
