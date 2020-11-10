package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//TODO: should these be immutable ?

// T is the resource type, and must be an enum
public class Bundle<T extends Enum<T>> {

    private final Map<T, BundleResource<T>> resources = new HashMap<>();

    public Bundle() { }

    public Bundle(final BundleResource<T> resource) {
        resources.put(resource.getResourceType(), resource);
    }

    public Bundle<T> add(final BundleResource<T> resource) {
        resources.put(resource.getResourceType(), resource);
        return this;
    }

    public Bundle<T> add(final Collection<BundleResource<T>> resources) {
        resources.forEach(this::add);
        return this;
    }

    public Optional<IOPath> get(final T target) {
        final BundleResource<T> bundleResource = resources.get(target);
        return bundleResource == null ? Optional.empty() : Optional.of(bundleResource.getResourcePath());
    }

}
