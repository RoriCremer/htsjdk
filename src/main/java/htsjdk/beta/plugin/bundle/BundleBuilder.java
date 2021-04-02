package htsjdk.beta.plugin.bundle;

import htsjdk.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder class for {@link Bundle}.
 */
public class BundleBuilder {

    //TODO: use List here in order to allow duplicates ?
    //TODO: add a test to ensure that duplicates work
    final List<BundleResource> bundleResources = new ArrayList<>();

    static public BundleBuilder start() {
        return new BundleBuilder();
    }

    protected BundleBuilder() { }

    public BundleBuilder add(final BundleResource resource) {
        ValidationUtils.nonNull(resource, "resource");
        bundleResources.add(resource);
        return this;
    }

    public Bundle getBundle() {
        return new Bundle(bundleResources);
    }
}


