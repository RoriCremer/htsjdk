package htsjdk.plugin.bundle;

import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.utils.ValidationUtils;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * A resource that can be added to an {@link InputBundle}.
 */
public abstract class InputResource extends BundleResource {

    public InputResource(
            final String contentType,
            final String displayName,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(contentType, displayName, tag, tagAttributes);
    }

    public abstract Optional<InputStream> getInputStream();

    public abstract Optional<SeekableStream> getSeekableStream();

    //TODO: isRandomAccess ? isQueryable ?
    public boolean isSeekable() { return false; }
}
