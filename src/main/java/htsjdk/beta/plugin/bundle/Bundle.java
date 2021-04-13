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

//TODO: bundles cannot be empty (if we decide to allow that, should they be serializable) ?
//TODO: should GATK propagate the @Argument tag and attributes to the primary resource in the bundle
//      on JSON deserialization ?
//TODO: should these classes live in the the bundle package, or beta.io package ?

//TODO: Should we try to validate that, say, a reads input bundle LOOKS like a reads bundle (i.e, that
//      its resources look like a recognizable sam/bam/cram/sra input) ? htsget URIs are hard to disambiguate...
//TODO: validate index content type as well as reads ?
//TODO: ReadsBundle subContentType is always inferred, never explicitly provided

//TODO: use GSON to get pretty printing ? (jar is about 275k; check other dependencies)
//TODO: need better JSON schema/versioning support
//TODO: add schema validation: https://github.com/bolerio/mjson/wiki/A-Tour-of-the-API#validating-with-json-schema
//TODO: the current serialization schema won't handle multiple resources with the same contentType key (i.e., two
//      index resources) unless we write them as a JSON array, since the contentType is the JSON property key

/**
 * An immutable collection of related resources (a primary resource, such as READS,
 * variants, features, or a reference, etc.), plus zero or more related companion resources (index,
 * dictionary, MD5, etc.).
 *
 * Each resource in a bundle is represented by a {@link BundleResource}, which in turn describes a binding
 * mechanism for that resource (such as an {@link IOPath}, in the case of a URI, Path or file name; a stream;
 * or a seekable stream), and a "content type" string such as "READS" that describes the content of that
 * resource. Any string can be used as a content type. Predefined content type strings are defined in {@link
 * BundleResourceType}.
 *
 * A valid bundle must have a "primary" key, which is a content type string that represents the primary resource
 * in that bundle. A bundle resource with the primary content type must be present in the bundle.
 *
 * Bundles that contain only serializable ({@link IOPathResource}) resources may be serialized to, and deserialized
 * from JSON.
 */
public class Bundle implements Iterable<BundleResource>, Serializable {
    public static final String BUNDLE_EXTENSION = ".json";
    private static final long serialVersionUID = 1L;
    private static final Log LOG = Log.getInstance(Bundle.class);

    public static final String JSON_PROPERTY_SCHEMA_NAME      = "schemaName";
    public static final String JSON_PROPERTY_SCHEMA_VERSION   = "schemaVersion";
    public static final String JSON_PROPERTY_PRIMARY          = "primary";
    public static final String JSON_PROPERTY_PATH             = "path";
    public static final String JSON_PROPERTY_SUB_CONTENT_TYPE = "subtype";
    public static final String JSON_SCHEMA_NAME               = "htsbundle";
    public static final String JSON_SCHEMA_VERSION            = "0.1.0"; // TODO: bump this to 1.0.0

    private final Map<String, BundleResource> resources = new HashMap<>();
    private final String primaryResourceKey;

    /**
     * @param primaryResourceKey the content type of the primary resource in this bundle. may not be null.
     *                           a resource with this content type must be included in resources
     * @param resources resources to include in this bundle, may not be null or empty
     */
    public Bundle(final String primaryResourceKey, final Collection<BundleResource> resources) {
        ValidationUtils.nonNull(primaryResourceKey, "primary resource");
        ValidationUtils.validateArg(primaryResourceKey.length() > 0,
                "A non-zero length primary resource key must be provided");
        ValidationUtils.nonNull(resources, "secondary resource collection");
        ValidationUtils.validateArg(!resources.isEmpty(), "A non-empty secondary resource collection must be provided");

        resources.forEach(r -> {
            if (null != this.resources.putIfAbsent(r.getContentType(), r)) {
                throw new IllegalArgumentException(
                        String.format("Attempt to add a duplicate resource for bundle key: %s", r.getContentType()));
            }
        });
        this.primaryResourceKey = primaryResourceKey;

        // validate that the primary resource actually exists in the resources
        if (!this.resources.containsKey(primaryResourceKey)) {
            throw new IllegalArgumentException(
                    String.format("Primary resource key %s is not present in the resource list", primaryResourceKey));
        }
    }

    /**
     * @param jsonString a valid JSON string conforming to the bundle schema
     */
    public Bundle(final String jsonString) {
        this(ValidationUtils.nonNull(jsonString, "resource list"), HtsPath::new);
    }

    /**
     *
     * @param jsonString a valid JSON string conforming to the bundle schema
     * @param ioPathConstructor a Function that takes a String and returns an object of a class that implements
     *                          IOPath. This can be used to create bundles from JSON that contain caller-specific
     *                          IOPath derived objects.
     */
    protected Bundle(final String jsonString, final Function<String, IOPath> ioPathConstructor) {
        ValidationUtils.nonNull(jsonString, "JSON string");
        ValidationUtils.nonNull(ioPathConstructor, "IOPath-derived class constructor");

        try {
            final Json jsonDocument = Json.read(jsonString);
            if (jsonDocument == null || jsonString.length() < 1) {
                throw new IllegalArgumentException(
                        String.format("JSON file parsing failed %s", jsonString));
            }

            // validate the schema name
            final String schemaName = getPropertyAsString(Bundle.JSON_PROPERTY_SCHEMA_NAME, jsonDocument);
            if (!schemaName.equals(Bundle.JSON_SCHEMA_NAME)) {
                throw new IllegalArgumentException(
                        String.format("Expected bundle schema name %s but found %s", Bundle.JSON_SCHEMA_NAME, schemaName));
            }

            // validate the schema version
            final String schemaVersion = getPropertyAsString(Bundle.JSON_PROPERTY_SCHEMA_VERSION, jsonDocument);
            if (!schemaVersion.equals(Bundle.JSON_SCHEMA_VERSION)) {
                throw new IllegalArgumentException(String.format("Expected bundle schema version %s but found %s",
                        Bundle.JSON_SCHEMA_VERSION, schemaVersion));
            }
            this.primaryResourceKey = getPropertyAsString(Bundle.JSON_PROPERTY_PRIMARY, jsonDocument);

            jsonDocument.asJsonMap().forEach((String contentType, Json jsonDoc) -> {
                if (!contentType.equals(Bundle.JSON_PROPERTY_SCHEMA_NAME) &&
                        !contentType.equals(Bundle.JSON_PROPERTY_SCHEMA_VERSION) &&
                        !contentType.equals(Bundle.JSON_PROPERTY_PRIMARY)) {
                    final Json subContentType = jsonDoc.at(Bundle.JSON_PROPERTY_SUB_CONTENT_TYPE);
                    final IOPathResource ioPathResource = new IOPathResource(
                            ioPathConstructor.apply(getPropertyAsString(Bundle.JSON_PROPERTY_PATH, jsonDoc)),
                            contentType,
                            subContentType == null ?
                                    null :
                                    getPropertyAsString(Bundle.JSON_PROPERTY_SUB_CONTENT_TYPE, jsonDoc));
                    resources.put(contentType, ioPathResource);
                }
            });
            if (this.resources.isEmpty()) {
                LOG.warn("Empty resource bundle found: ", jsonString);
            }
        } catch (Json.MalformedJsonException | java.lang.UnsupportedOperationException e) {
            throw new IllegalArgumentException(e);
        }

        // validate that the primary resource actually exists in the resources
        if (!this.resources.containsKey(this.primaryResourceKey)) {
            throw new IllegalArgumentException(
                    String.format(
                            "The resource specified by the primary property is not present in the resource list %s",
                            primaryResourceKey));
        }
    }

    /**
     * Return the BundleResource for the provided targetContentType string.
     *
     * @param targetContentType the content type to be retrieved from the bundle
     * @return an Optional<BundleResource> that contains the targetContent type
     */
    public Optional<BundleResource> get(final String targetContentType) {
        ValidationUtils.nonNull(targetContentType, "target content string");
        return Optional.ofNullable(resources.get(targetContentType));
    }

    /**
     * Return the primary content type for this bundle.
     * @return the primary content type for this bundle
     */
    public String getPrimaryResourceKey() { return primaryResourceKey; }

    /**
     * Return the primary {@link BundleResource} for this bundle.
     * @return the primary {@link BundleResource} for this bundle.
     */
    public BundleResource getPrimaryResource() {
        return resources.get(primaryResourceKey);
    }

    /**
     * If true, you can obtain an InputStream for each resource in the bundle
     * @return true if all resources in the bundle can be used for input
     */
    public boolean isInputBundle() { return resources.values().stream().allMatch(BundleResource::isInputResource); }

    /**
     * If true, you can get obtain an OutputStream for each resource in the bundle
     * @return true if all resources in the bundle can be used for input
     */
    public boolean isOutputBundle() { return !isInputBundle(); }

    /**
     * Obtain an iterator of BundleResources for this bundle.
     * @return iterator of BundleResources for this bundle.
     */
    public Iterator<BundleResource> iterator() { return resources.values().iterator(); }

    /**
     * Serialize this bundle to a JSON string representation. All resources in the bundle must
     * be {@link IOPathResource} for serialization to succeed.
     *
     * @return a JSON representation of this bundle
     */
    public String toJSON() {
        final Json outerJSON = Json.object()
                .set(JSON_PROPERTY_SCHEMA_NAME, JSON_SCHEMA_NAME)
                .set(JSON_PROPERTY_SCHEMA_VERSION, JSON_SCHEMA_VERSION)
                .set(JSON_PROPERTY_PRIMARY, getPrimaryResourceKey());

        resources.keySet().forEach(key -> {
            final BundleResource bundleResource = resources.get(key);
            final Optional<IOPath> resourcePath = bundleResource.getIOPath();
            if (!resourcePath.isPresent()) {
                throw new IllegalArgumentException("Bundle resource requires a valid path to be serialized");
            }

            // generate JSON for each bundle resource
            final Json resourceJSON = Json.object().set(JSON_PROPERTY_PATH, resourcePath.get().toString());
            if (bundleResource.getSubContentType().isPresent()) {
                resourceJSON.set(JSON_PROPERTY_SUB_CONTENT_TYPE, bundleResource.getSubContentType().get());
            }
            outerJSON.set(bundleResource.getContentType(), resourceJSON);
        });
        return outerJSON.toString();
    }

    private String getPropertyAsString(final String propertyName, final Json jsonDocument) {
        final Json propertyValue = jsonDocument.at(propertyName);
        if (propertyValue == null) {
            throw new IllegalArgumentException(
                    String.format("JSON bundle is missing the required property %s (%s)",
                            propertyName,
                            jsonDocument.toString()));
        } else if (!propertyValue.isString()) {
            throw new IllegalArgumentException(
                    String.format("Expected string value for bundle property %s but found %s",
                            propertyName,
                            propertyValue.toString()));
        }
        return propertyValue.asString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bundle that = (Bundle) o;

        if (!resources.equals(that.resources)) return false;
        return primaryResourceKey.equals(that.primaryResourceKey);
    }

    @Override
    public int hashCode() {
        int result = resources.hashCode();
        result = 31 * result + primaryResourceKey.hashCode();
        return result;
    }
}
