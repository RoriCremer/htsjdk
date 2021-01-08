package htsjdk.plugin.bundle;

import htsjdk.utils.ValidationUtils;

import java.util.List;

/**
 * Builder class for {@link InputBundle}s.
 */
public class InputBundleBuilder extends BundleBuilder<InputResource> {

    static public InputBundleBuilder start() {
        return new InputBundleBuilder();
    }

    final public InputBundleBuilder add(final InputResource resource) {
        ValidationUtils.nonNull(resource, "A non-null resource is required");
        bundleResources.add(resource);
        return this;
    }

    final public InputBundleBuilder add(final List<InputResource> resources) {
        ValidationUtils.nonNull(resources, "A non-null resource list is required");
        ValidationUtils.nonEmpty(resources, "A non-empty resource list is required");
        bundleResources.addAll(resources);
        return this;
    }

    final public InputBundle getBundle() {
        return new InputBundle(bundleResources);
    }
}
