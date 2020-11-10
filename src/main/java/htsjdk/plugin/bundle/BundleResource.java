package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;

// T is a resource type enum

public interface BundleResource<T> {
    IOPath getResourcePath();

    T getResourceType();
}
