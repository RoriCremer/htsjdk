package htsjdk.beta.plugin.bundle;

import htsjdk.utils.ValidationUtils;

/**
 * Builder class for {@link OutputBundle}s.
 */
public final class OutputBundleBuilder extends BundleBuilder<OutputResource> {

    static public OutputBundleBuilder start() {
        return new OutputBundleBuilder();
    }

    public OutputBundleBuilder add(final OutputResource resource) {
        ValidationUtils.nonNull(resource, "resource");
        bundleResources.add(resource);
        return this;
    }

    public OutputBundle getBundle() {
        return new OutputBundle(bundleResources);
    }

}
