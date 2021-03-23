package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.samtools.util.Log;
import htsjdk.utils.ValidationUtils;
import mjson.Json;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

//    {
//        "schemaType": "ReadsBundle",
//        "schemaVersion": "0.1.0",
//        "reads":{"path":"a file", "fileType":"bam"},
//        "index":{"path":"an index", "fileType":"bai"}
//    }
//    {
//        "schemaName":"htsbundle",
//        "schemaVersion":"0.1.0",
//        "READS":{"path":"myFile.bam","subtype":"NONE"}
//        "INDEX":{"path":"myFile.bai","subtype":"NONE"},
//    }

//TODO: fix InputIOPathResource getSeekableStream() to create a seekable stream from underlying file/path
//TODO: toString()

//TODO JSON:
//TODO: use GSON to get pretty printing ? (jar is about 275k, but other dependencies)
//TODO: serialize attributes, tags ?
//TODO: need better JSON schema/versioning support
//TODO: add validatation against a schema:
//  https://github.com/bolerio/mjson/wiki/A-Tour-of-the-API#validating-with-json-schema
//TODO: if we use contentType as the json property key, it won't handle multiple resources with the
// same key (i.e., two index resources); or else we need to write them as a JSON array

/**
 * Aa immutable container for a collection of related resources (a primary resource such as reads,
 * variants, features, or reference, etc.), plus zero or more related companion resources (index,
 * dictionary, MD5, etc.). This is essentially a more generic version of SamInputResource, suitable
 * for handling both inputs and output resources for any codec type).
 *
 * Each resource is represented by a BundleResource, which in turn describes a binding mechanism
 * for that resource (such as an HtsPath (for URI, Path or file name), or stream).
 *
 * A serialized bundles is not marked as input vs output - its just a bundle.
 * There is a single schema for all serialized bundles that is not specific to READS, REFERENCE.
 *
 * @param <T> a type that determines whether this bundle contains InputResources (that can be read)
 *           or OutputResources (that can be written)
 */
public abstract class Bundle<T extends BundleResource> implements Iterable<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Log LOG = Log.getInstance(Bundle.class);

    public static String JSON_SCHEMA_NAME_PROPERTY = "schemaName";
    public static String JSON_VERSION_PROPERTY = "schemaVersion";
    public static String JSON_PATH_PROPERTY = "path";
    public static String JSON_SUBCONTENT_PROPERTY = "subtype";
    public static String JSON_SCHEMA_NAME = "htsbundle";
    public static String JSON_SCHEMA_VERSION = "0.1.0";

    private final Map<String, T> resources = new HashMap<>();

    public Bundle(final Collection<T> resources) {
        ValidationUtils.nonNull(resources, "resource collection");
        ValidationUtils.validateArg(!resources.isEmpty(), "non empty resource collection must be provided");
        resources.forEach(r -> {
            if (this.resources.containsKey(r.getContentType())) {
                throw new IllegalArgumentException(
                        String.format("Attempt to add a duplicate resource for bundle key: %s", r.getContentType()));
            }
            this.resources.put(r.getContentType(), r);
        });
    }

    protected Bundle(final String jsonString, final Function<String, IOPath> customPathConstructor) {
        ValidationUtils.nonNull(jsonString, "JSON string");

        try {
            final mjson.Json jsonDocument = Json.read(jsonString);
            if (jsonDocument == null || jsonString.length() < 1) {
                throw new IllegalArgumentException(
                        String.format("JSON file parsing failed %s", jsonString));
            }

            final mjson.Json schemaName = jsonDocument.at(Bundle.JSON_SCHEMA_NAME_PROPERTY);
            if (schemaName == null) {
                throw new IllegalArgumentException(
                        String.format("JSON file is missing the required property %s", Bundle.JSON_SCHEMA_NAME_PROPERTY));
            } else if (!schemaName.isString() || !schemaName.asString().equals(Bundle.JSON_SCHEMA_NAME)) {
                throw new IllegalArgumentException(
                        String.format("Expected bundle schema %s but found %s", Bundle.JSON_SCHEMA_NAME, schemaName));
            }

            final mjson.Json schemaVersion = jsonDocument.at(Bundle.JSON_VERSION_PROPERTY);
            if (schemaVersion == null) {
                throw new IllegalArgumentException(String.format("Bundle JSON is missing required property %s",
                        Bundle.JSON_VERSION_PROPERTY));
            } else if (!schemaVersion.isString() || !schemaVersion.asString().equals(Bundle.JSON_SCHEMA_VERSION)) {
                throw new IllegalArgumentException(String.format("Expected bundle version %s but found %s",
                        Bundle.JSON_SCHEMA_VERSION, schemaVersion));
            }

            jsonDocument.asMap().forEach((String key, Object doc) -> {
                if (!key.equals(Bundle.JSON_SCHEMA_NAME_PROPERTY) && !key.equals(Bundle.JSON_VERSION_PROPERTY)) {
                    if (doc == null) {
                        throw new IllegalArgumentException(
                                String.format("Missing value for JSON property %s", key));
                    }
                    if (doc instanceof Map) {
                        resources.put(key, createBundleResourceFromJSON(key, (Map<String, Object>) doc, customPathConstructor));
                    } else {
                        throw new IllegalArgumentException(
                                String.format("Expected a map value but got %s for JSON property %s", doc, key));
                    }
                }
            });
            if (resources.isEmpty()) {
                LOG.warn("Empty resource bundle found: ", jsonString);
            }
        } catch (mjson.Json.MalformedJsonException | java.lang.UnsupportedOperationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected abstract T createBundleResourceFromJSON(
            final String contentType,
            final Map<String, Object> doc,
            final Function<String, IOPath> customPathConstructor);

    protected String getJSONPropertyAsString(final Map<String, Object> jsonElement, final String propertyName) {
        final Object element = jsonElement.get(propertyName);
        if (element == null) {
            throw new IllegalArgumentException(String.format("Property %s is missing", propertyName));
        } else if (element instanceof Map) {
            throw new IllegalArgumentException(String.format("Expected a string value for %s but got %s", propertyName, element));
        }
        return element.toString();
    }

    public Optional<T> get(final String targetContent) {
        ValidationUtils.nonNull(targetContent, "target content string");
        return Optional.ofNullable(resources.get(targetContent));
    }

    public Iterator<T> iterator() {
        return resources.values().iterator();
    }

    public String toJSON() {
        final mjson.Json outerJSON = mjson.Json.object()
                .set(JSON_SCHEMA_NAME_PROPERTY, JSON_SCHEMA_NAME)
                .set(JSON_VERSION_PROPERTY, JSON_SCHEMA_VERSION);

        resources.keySet().forEach(key -> {
            final BundleResource bundleResource = resources.get(key);
            final Optional<IOPath> resourcePath = bundleResource.getIOPath();
            if (!resourcePath.isPresent()) {
                throw new IllegalArgumentException("Bundle resource requires a valid path to be serialized");
            }
            outerJSON.set(bundleResource.getContentType(),
                    bundleResource.getSubContentType().isPresent() ?
                        mjson.Json.object()
                                .set(JSON_PATH_PROPERTY, resourcePath.get().toString())
                                .set(JSON_SUBCONTENT_PROPERTY, bundleResource.getSubContentType().get()) :
                        mjson.Json.object()
                                .set(JSON_PATH_PROPERTY, resourcePath.get().toString()));
        });
        return outerJSON.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bundle<?> bundle = (Bundle<?>) o;

        return resources.equals(bundle.resources);
    }

    @Override
    public int hashCode() {
        return resources.hashCode();
    }
}
