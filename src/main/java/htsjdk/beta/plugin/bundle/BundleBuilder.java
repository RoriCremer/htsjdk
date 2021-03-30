package htsjdk.beta.plugin.bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder class for {@link Bundle}.
 *
 * @param <T> the type of resource the {@link Bundle} being built holds ({@link InputResource}
 *          or {@link OutputResource}).
 */
public abstract class BundleBuilder<T extends BundleResource> {

    //use a List in order to allow duplicates
    final List<T> bundleResources = new ArrayList<>();

    protected BundleBuilder() { }
}


