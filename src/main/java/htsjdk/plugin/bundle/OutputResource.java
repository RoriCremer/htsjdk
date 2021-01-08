package htsjdk.plugin.bundle;

import htsjdk.utils.ValidationUtils;

import java.io.OutputStream;
import java.util.Optional;

/**
 * A resource that can be added to an {@link OutputBundle}.
 */
public abstract class OutputResource extends BundleResource {

    public OutputResource(final String contentType, final String displayName) {
        super(ValidationUtils.nonNull(contentType, "A content type must be provided"),
              ValidationUtils.nonNull(displayName, "A display name must be provided"));
    }

    public abstract Optional<OutputStream> getOutputStream();

}
