package htsjdk.plugin.bundle;

import java.util.List;

/**
 * A bundle containing input resources for use with {@link htsjdk.plugin.HtsDecoder}s.
 */
public class InputBundle extends Bundle<InputResource> {

    public InputBundle(final List<InputResource> resources) {
        super(resources);
    }

    //TODO: toString, equals, hashCode
}
