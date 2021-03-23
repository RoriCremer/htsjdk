package htsjdk.plugin.bundle;

import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A bundle containing input resources for use with {@link htsjdk.plugin.HtsDecoder}s.
 */
public class InputBundle extends Bundle<InputResource> implements Serializable {
    private static final long serialVersionUID = 1L;

    // only allow InputBundleBuilder to call this to prevent List<OutputResource> from
    // being passed in
    public InputBundle(final List<InputResource> resources) {
        super(ValidationUtils.nonNull(resources, "resource list"));
    }

    public InputBundle(final String jsonString) {
        this(ValidationUtils.nonNull(jsonString, "resource list"), HtsPath::new);
    }

    public InputBundle(final String jsonString, final Function<String, IOPath> customPathConstructor) {
        super(ValidationUtils.nonNull(jsonString, "resource list"), customPathConstructor);
    }

    @Override
    public InputResource createBundleResourceFromJSON(
            final String contentType,
            final Map<String, Object> jsonMap,
            final Function<String, IOPath> customPathConstructor) {
        final Object subType = jsonMap.get(Bundle.JSON_SUBCONTENT_PROPERTY);
        if (subType != null ) {
            return new InputIOPathResource(
                    customPathConstructor.apply(getJSONPropertyAsString(jsonMap, Bundle.JSON_PATH_PROPERTY)),
                    contentType,
                    getJSONPropertyAsString(jsonMap, Bundle.JSON_SUBCONTENT_PROPERTY));
        } else {
            return new InputIOPathResource(
                    customPathConstructor.apply(getJSONPropertyAsString(jsonMap, Bundle.JSON_PATH_PROPERTY)),
                    contentType);
        }
    }
}
