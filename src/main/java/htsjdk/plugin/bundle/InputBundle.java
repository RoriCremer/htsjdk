package htsjdk.plugin.bundle;

import htsjdk.utils.ValidationUtils;

import java.util.List;

/**
 * A bundle containing input resources for use with {@link htsjdk.plugin.HtsDecoder}s.
 */
public class InputBundle extends Bundle<InputResource> {

    public InputBundle(final List<InputResource> resources) {
        super(ValidationUtils.nonNull(resources, "A non-null resources list is required"));
    }

}
