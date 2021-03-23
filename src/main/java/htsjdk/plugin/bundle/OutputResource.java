package htsjdk.plugin.bundle;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * A immutable resource that can be added to an {@link OutputBundle}. This is basically just a tagging
 * interface.
 */
public abstract class OutputResource extends BundleResource implements Serializable {
    private static final long serialVersionUID = 1L;

    public OutputResource(
            final String displayName,
            final String contentType,
            final String subContentType,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(displayName, contentType, subContentType, tag, tagAttributes);
    }

    public abstract Optional<OutputStream> getOutputStream();

}