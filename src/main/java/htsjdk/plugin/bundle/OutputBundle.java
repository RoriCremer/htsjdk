package htsjdk.plugin.bundle;

import htsjdk.utils.ValidationUtils;

import java.util.List;

/**
 * A bundle containing input resources for use with {@link htsjdk.plugin.HtsEncoder}s.
 */
public class OutputBundle<T extends OutputResource> extends Bundle<T> {

    public OutputBundle(final List<T> resources) {
        super(ValidationUtils.nonNull(resources, "A non-null resources list is required"));
    }

}
