package htsjdk.plugin.bundle;

import java.io.OutputStream;
import java.util.Optional;

/**
 * A resource that can be added to an {@link OutputBundle}.
 */
public abstract class OutputResource extends BundleResource {

    public OutputResource(final String contentType, final String displayName) {
        super(contentType, displayName);
    }

    public abstract Optional<OutputStream> getOutputStream();

    //TODO: toString, hashCode

}
