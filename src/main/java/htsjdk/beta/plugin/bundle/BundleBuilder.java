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
    private final List<BundleResource> resources = new ArrayList<>();
    private String primaryResource;

    static public BundleBuilder start() {
        return new BundleBuilder();
    }

    protected BundleBuilder() { }

    public BundleBuilder addPrimary(final BundleResource resource) {
        ValidationUtils.nonNull(resource, "resource");
        add(resource);
        if (primaryResource != null) {
                throw new IllegalStateException(String.format(
                        "Can't add primary resource %s to a bundle that already has primary resource %s",
                        resource.getContentType(),
                        primaryResource));
        }
        primaryResource = resource.getContentType();
        return this;
    }

    public BundleBuilder add(final BundleResource resource) {
        ValidationUtils.nonNull(resource, "resource");
        resources.add(resource);
        return this;
    }

    public Bundle getBundle() {
        if (primaryResource == null) {
            throw new IllegalStateException("A bundle must have a primary resource.");
        }
        return new Bundle(primaryResource, resources);
    }
}


