package htsjdk.plugin.bundle;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Aa immutable container for a collection of related resources (a primary resource such as reads,
 * variants, features, or reference, etc.), plus zero or more related companion resources (index,
 * dictionary, MD5, etc.). It is essentially a more generic version of SamInputResource, suitable
 * for handling both inputs and output resources for any codec type).
 *
 * Each resource is represented by a BundleResource, which in turn describes an binding mechanism
 * for that resource (such as a URI, Path, file name, or stream).
 *
 * @param <T> a type that determines whether this bundle contains InputResources or OutputResources.
 */
public abstract class Bundle<T extends BundleResource> {

    public static String JSON_SCHEMA_NAME = "htsbundle";
    public static String JSON_SCHEMA_VERSION = "0.1.0";
    public static String JSON_PROPERTY_EMPTY = "NONE";

    private final Map<String, T> resources = new HashMap<>();

    public Bundle(final Collection<T> resources) {
        ValidationUtils.nonNull(resources, "non-null resource collection must be provided");
        ValidationUtils.validateArg(!resources.isEmpty(), "non empty resource collection must be provided");
        resources.forEach(t -> {
            if (this.resources.containsKey(t.getContentType())) {
                throw new IllegalArgumentException(
                        String.format("Attempt to add a duplicate resource for bundle key: %s", t.getContentType()));
            }
            this.resources.put(t.getContentType(), t);
        });
    }

    public Optional<T> get(final String targetContent) {
        ValidationUtils.nonNull(targetContent, "target content string must be provided");
        return Optional.ofNullable(resources.get(targetContent));
    }

    //TODO: this needs proper schema/versioning support
    public String serializeToJSON() {
        mjson.Json json = mjson.Json.object()
                .set("name", JSON_SCHEMA_NAME)
                .set("version", JSON_SCHEMA_VERSION);
        resources.keySet().forEach(r -> {
            final Optional<IOPath> rPath = resources.get(r).getIOPath();
            if (rPath.isPresent()) {
                json.set(r, rPath.get().toString());
            } else {
                json.set(r, JSON_PROPERTY_EMPTY);
            }
        });
        return json.toString();
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
