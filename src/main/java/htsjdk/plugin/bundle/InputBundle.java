package htsjdk.plugin.bundle;

import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A bundle containing input resources for use with {@link htsjdk.plugin.HtsDecoder}s.
 */
public class InputBundle extends Bundle<InputResource> implements Serializable {
    private static final long serialVersionUID = 1L;

    // Shorthand constructors for the simple common IOPath cases
    public InputBundle(final IOPath ioPath, final String contentType) {
        this(ioPath, contentType, null);
    }

    public InputBundle(final IOPath ioPath, final String contentType, final String subContentType) {
        this(ioPath, contentType, subContentType, null);
    }

    public InputBundle(final IOPath ioPath, final String contentType, final String subContentType, final String tag) {
        this(ioPath, contentType, subContentType, tag, null);
    }

    public InputBundle(
            final IOPath ioPath,
            final String contentType,
            final String subContentType,
            final String tag,
            final Map<String,
                    String> attributes) {
        this(Collections.unmodifiableList(Arrays.asList(
                new InputIOPathResource(ioPath, contentType, subContentType, tag, attributes))
        ));
    }

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
        ValidationUtils.nonNull(contentType, "content type string");
        ValidationUtils.nonNull(jsonMap, "json document map");
        ValidationUtils.nonNull(customPathConstructor, "IOPath-derived class constructor");

        return new InputIOPathResource(
                customPathConstructor.apply(getJSONPropertyAsString(jsonMap, Bundle.JSON_PROPERTY_PATH)),
                contentType,
                jsonMap.get(Bundle.JSON_PROPERTY_SUB_CONTENT_TYPE) == null ?
                        null :
                        getJSONPropertyAsString(jsonMap, Bundle.JSON_PROPERTY_SUB_CONTENT_TYPE),
                jsonMap.get(Bundle.JSON_PROPERTY_TAG) == null ?
                        null :
                        getJSONPropertyAsString(jsonMap, Bundle.JSON_PROPERTY_TAG),
                jsonMap.get(Bundle.JSON_PROPERTY_ATTRIBUTES) == null ?
                        null :
                        (Map<String, String>) jsonMap.get(Bundle.JSON_PROPERTY_ATTRIBUTES));
    }

}
