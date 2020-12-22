package htsjdk.plugin.bundle;

import java.util.List;

/**
 * A bundle containing input resources for use with {@link htsjdk.plugin.HtsEncoder}s.
 */
public class OutputBundle<T extends OutputResource> extends Bundle<T> {

    public OutputBundle(final List<T> resources) {
        super(resources);
    }

    //TODO: toString, equals, hashCode
}
