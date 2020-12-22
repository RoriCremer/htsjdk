package htsjdk.plugin.bundle;


import htsjdk.io.IOPath;

import java.util.Optional;

/**
 * Base class for {@link InputResource} or {@link OutputResource}.
 */
public abstract class BundleResource {
    private final String displayName;
    private final String contentType;

    public BundleResource(final String contentType, final String displayName) {
        this.contentType = contentType;
        this.displayName = displayName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDisplayName() { return displayName; }

    public Optional<IOPath> getIOPath() { return Optional.empty(); };   // may be None for some sub-types

}
