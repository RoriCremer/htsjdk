package htsjdk.plugin.bundle;

import htsjdk.utils.ValidationUtils;

/**
 * Builder class for {@link InputBundle}s.
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
