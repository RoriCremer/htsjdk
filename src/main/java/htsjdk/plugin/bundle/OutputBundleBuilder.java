package htsjdk.plugin.bundle;

/**
 * Builder class for {@link OutputBundle}s.
 */
public class OutputBundleBuilder extends BundleBuilder<OutputResource> {

    static public InputBundleBuilder start() {
        return new InputBundleBuilder();
    }

    final public OutputBundle getBundle() {
        return new OutputBundle(bundleResources);
    }
}
