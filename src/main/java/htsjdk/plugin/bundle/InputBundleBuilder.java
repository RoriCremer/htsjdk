package htsjdk.plugin.bundle;

import java.util.List;

/**
 * Builder class for {@link InputBundle}s.
 */
public class InputBundleBuilder extends BundleBuilder<InputResource> {

    static public InputBundleBuilder start() {
        return new InputBundleBuilder();
    }

    final public InputBundleBuilder add(final InputResource resource) {
        bundleResources.add(resource);
        return this;
    }

    final public InputBundleBuilder add(final List<InputResource> resources) {
        bundleResources.addAll(resources);
        return this;
    }

    final public InputBundle getBundle() {
        //TODO: assert that there are resources...or maybe do that right in Bundle
        return new InputBundle(bundleResources);
    }
}
