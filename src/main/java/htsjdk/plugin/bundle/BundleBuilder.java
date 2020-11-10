package htsjdk.plugin.bundle;

import java.util.Collection;

public class BundleBuilder<T extends Enum<T>> {

    final private Bundle<T> bundle = new Bundle();

    private BundleBuilder() {};

    static public <T extends Enum<T>> BundleBuilder<T> get() { return new BundleBuilder<T>(); }

    final BundleBuilder add(final BundleResource<T> resource) {
        bundle.add(resource);
        return this;
    }

    final BundleBuilder add(final Collection<BundleResource<T>> resources) {
        bundle.add(resources);
        return this;
    }
}
