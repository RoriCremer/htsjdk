package htsjdk.plugin.bundle;

import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

/**
 * A resource that can be added to an {@link OutputBundle}. This is basically just a tagging
 * interface
 */
public abstract class OutputResource extends BundleResource {

    public OutputResource(
            final String contentType,
            final String displayName,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(contentType, displayName, tag, tagAttributes);
    }

    public abstract Optional<OutputStream> getOutputStream();

}
