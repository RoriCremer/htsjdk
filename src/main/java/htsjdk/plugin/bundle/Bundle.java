package htsjdk.plugin.bundle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Aa immutable container for a collection of related resources (a primary resource such as reads,
 * variants, features, or reference, etc.), plus zero or more related companion resources (an index, dictionary,
 * MD5, etc). It is essentially a more generic version of SamInputResource, suitable for handling both
 * inputs and output resources for any codec type).
 *
 * Each resource is represented by a BundleResource, which in turn describes an access mechanism for that resource
 * (such as a URI, Path, file name, or stream).
 *
 * @param <T> a type that determines whether this bundle contains InputResources or OutputResources.
 */

// TODO: do we ever need duplicates/collections (i.e. 2 index files, etc.) ?

class Bundle<T extends BundleResource> {

    private final Map<String, T> resources = new HashMap<>();

    public Bundle(final Collection<T> resources) {
        resources.forEach(t -> this.resources.put(t.getContentType(), t));
    }

    public Optional<T> get(final String targetContent) {
        return Optional.ofNullable(resources.get(targetContent));
    }

}
