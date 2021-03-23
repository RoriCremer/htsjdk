package htsjdk.plugin.bundle;

import htsjdk.utils.ValidationUtils;

/**
 * Builder class for {@link InputBundle}s.
 * - immutable
 * - no List<T> constructor since it allows other subclasses (OutputResources)
 */
public final class InputBundleBuilder extends BundleBuilder<InputResource> {

    static public InputBundleBuilder start() {
        return new InputBundleBuilder();
    }

    public InputBundleBuilder add(final InputResource resource) {
        ValidationUtils.nonNull(resource, "resource");
        bundleResources.add(resource);
        return this;
    }

    public InputBundle getBundle() {
        return new InputBundle(bundleResources);
    }

}
