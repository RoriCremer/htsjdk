package htsjdk.beta.plugin.bundle;

import htsjdk.io.HtsPath;
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

// Original GATK branch:
//
//    {
//        "schemaType": "ReadsBundle",
//        "schemaVersion": "0.1.0",
//        "reads":{"path":"a file", "fileType":"bam"},
//        "index":{"path":"an index", "fileType":"bai"}
//    }
//
// Htsjdk, simple bundle:
//
//    {
//        "schemaName":"htsbundle",
//        "schemaVersion":"0.1.0",
//        "READS":{"path":"myFile.bam","subtype":"BAM", "keypath":"keyfile"}
//        "INDEX":{"path":"myFile.csi","subtype":"CSI"},
//    }
//
// Htsjdk, bundle with tag and attributes:
//
//  {
//      "schemaVersion":"0.1.0",
//      "schemaName":"htsbundle",
//      "primary":"READS",
//      "READS":{
//          "path":"my.bam",
//          "subtype":"BAM",
//          "tag":"testTAG",
//          "attributes":{"attribute1":"value1","attribute2":"value2"}
//      }
//   }

//TODO these should live in the IO package
//TODO: schema won't handle multiple resources with the same contentType key
//TODO: use GSON to get pretty printing ? (jar is about 275k; check other dependencies)
//TODO: need better JSON schema/versioning support
//TODO: add schema validation: https://github.com/bolerio/mjson/wiki/A-Tour-of-the-API#validating-with-json-schema
//TODO: the current serialization schema won't handle multiple resources with the same contentType key (i.e., two
//      index resources) unless we write them as a JSON array, since the contentType is the JSON property key
//TODO: are JSON names case sensitive?

/**
 * Aa immutable container for a collection of related resources (a primary resource such as reads,
 * variants, features, or a reference, etc.), plus zero or more related companion resources (index,
 * dictionary, MD5, etc.). This is essentially a more generic version of SamInputResource, suitable
 * for handling both inputs and output resources for any codec type).
 *
 * Each resource is represented by a BundleResource, which in turn describes a binding mechanism
 * for that resource (such as an HtsPath, in the case of a URI, Path or file name); a stream; or a
 * seekable stream.
 *
 * There is a single schema for all serialized bundles that is not specific to READS, REFERENCE.
 *
 */
public class Bundle implements Iterable<BundleResource>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Log LOG = Log.getInstance(Bundle.class);

    public static String JSON_PROPERTY_SCHEMA_NAME      = "schemaName";
    public static String JSON_PROPERTY_SCHEMA_VERSION   = "schemaVersion";
    public static String JSON_PROPERTY_PATH             = "path";
    public static String JSON_PROPERTY_SUB_CONTENT_TYPE = "subtype";

    public static String JSON_SCHEMA_NAME = "htsbundle";
    public static String JSON_SCHEMA_VERSION = "0.1.0"; // TODO: bump this to 1.0.0

    private final Map<String, BundleResource> resources = new HashMap<>();

    public Bundle(final Collection<BundleResource> resources) {
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

    public Bundle(final String jsonString) {
        this(ValidationUtils.nonNull(jsonString, "resource list"), HtsPath::new);
    }

    //TODO: move the schema code to code somewhere that handles schemas by version
    protected Bundle(final String jsonString, final Function<String, IOPath> customPathConstructor) {
        ValidationUtils.nonNull(jsonString, "JSON string");
        ValidationUtils.nonNull(customPathConstructor, "IOPath-derived class constructor");

        try {
            final mjson.Json jsonDocument = Json.read(jsonString);
            if (jsonDocument == null || jsonString.length() < 1) {
                throw new IllegalArgumentException(
                        String.format("JSON file parsing failed %s", jsonString));
            }

            final mjson.Json schemaName = jsonDocument.at(Bundle.JSON_PROPERTY_SCHEMA_NAME);
            if (schemaName == null) {
                throw new IllegalArgumentException(
                        String.format("JSON file is missing the required property %s", Bundle.JSON_PROPERTY_SCHEMA_NAME));
            } else if (!schemaName.isString() || !schemaName.asString().equals(Bundle.JSON_SCHEMA_NAME)) {
                throw new IllegalArgumentException(
                        String.format("Expected bundle schema %s but found %s", Bundle.JSON_SCHEMA_NAME, schemaName));
            }

            final mjson.Json schemaVersion = jsonDocument.at(Bundle.JSON_PROPERTY_SCHEMA_VERSION);
            if (schemaVersion == null) {
                throw new IllegalArgumentException(String.format("Bundle JSON is missing required property %s",
                        Bundle.JSON_PROPERTY_SCHEMA_VERSION));
            } else if (!schemaVersion.isString() || !schemaVersion.asString().equals(Bundle.JSON_SCHEMA_VERSION)) {
                throw new IllegalArgumentException(String.format("Expected bundle version %s but found %s",
                        Bundle.JSON_SCHEMA_VERSION, schemaVersion));
            }

            jsonDocument.asMap().forEach((String key, Object doc) -> {
                if (!key.equals(Bundle.JSON_PROPERTY_SCHEMA_NAME) && !key.equals(Bundle.JSON_PROPERTY_SCHEMA_VERSION)) {
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

    public Optional<BundleResource> get(final String targetContent) {
        ValidationUtils.nonNull(targetContent, "target content string");
        return Optional.ofNullable(resources.get(targetContent));
    }

    // if true, you can get an InputStream for each resource in the bundle
    public boolean isInputBundle() { return resources.values().stream().allMatch(BundleResource::isInputResource); }

    // if true, you can get an OutputStream for each resource in the bundle
    public boolean isOutputBundle() { return !isInputBundle(); }

    public Iterator<BundleResource> iterator() {
        return resources.values().iterator();
    }

    /**
     * Note: only IOPath resources can be serialized to JSON...
     */
    public String toJSON() {
        final mjson.Json outerJSON = mjson.Json.object()
                .set(JSON_PROPERTY_SCHEMA_NAME, JSON_SCHEMA_NAME)
                .set(JSON_PROPERTY_SCHEMA_VERSION, JSON_SCHEMA_VERSION);

        resources.keySet().forEach(key -> {
            final BundleResource bundleResource = resources.get(key);
            final Optional<IOPath> resourcePath = bundleResource.getIOPath();
            if (!resourcePath.isPresent()) {
                throw new IllegalArgumentException("Bundle resource requires a valid path to be serialized");
            }
            // generate JSON for each bundle resource
            final mjson.Json resourceJSON = mjson.Json.object().set(JSON_PROPERTY_PATH, resourcePath.get().toString());
            if (bundleResource.getSubContentType().isPresent()) {
                resourceJSON.set(JSON_PROPERTY_SUB_CONTENT_TYPE, bundleResource.getSubContentType().get());
            }
            outerJSON.set(bundleResource.getContentType(), resourceJSON);
        });
        return outerJSON.toString();
    }

    protected BundleResource createBundleResourceFromJSON(
            final String contentType,
            final Map<String, Object> jsonMap,
            final Function<String, IOPath> customPathConstructor) {
        ValidationUtils.nonNull(contentType, "content type string");
        ValidationUtils.nonNull(jsonMap, "json document map");
        ValidationUtils.nonNull(customPathConstructor, "IOPath-derived class constructor");

        return new IOPathResource(
                customPathConstructor.apply(getJSONPropertyAsString(jsonMap, Bundle.JSON_PROPERTY_PATH)),
                contentType,
                jsonMap.get(Bundle.JSON_PROPERTY_SUB_CONTENT_TYPE) == null ?
                        null :
                        getJSONPropertyAsString(jsonMap, Bundle.JSON_PROPERTY_SUB_CONTENT_TYPE));
    }

    protected String getJSONPropertyAsString(final Map<String, Object> jsonElement, final String propertyName) {
        final Object element = jsonElement.get(propertyName);
        if (element == null) {
            throw new IllegalArgumentException(String.format("Property %s is missing", propertyName));
        } else if (element instanceof Map) {
            throw new IllegalArgumentException(
                    String.format("Expected a string value for %s but got %s", propertyName, element));
        }
        return element.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bundle bundle = (Bundle) o;

        return resources.equals(bundle.resources);
    }

    @Override
    public int hashCode() {
        return resources.hashCode();
    }

}
