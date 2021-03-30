package htsjdk.beta.plugin.bundle;

import htsjdk.samtools.seekablestream.SeekableStream;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * An immutable resource that can be added to an {@link InputBundle}.
 */
public abstract class InputResource extends BundleResource implements Serializable {
    private static final long serialVersionUID = 1L;

    public InputResource(
            final String displayName,
            final String contentType,
            final String subContentType,
            final String tag,
            final Map<String, String> tagAttributes) {
        super(displayName, contentType, subContentType, tag, tagAttributes);
    }

    public abstract Optional<InputStream> getInputStream();

    public abstract Optional<SeekableStream> getSeekableStream();

    //TODO: rename this to isRandomAccess ? isQueryable ?
    /**
     * If isSeekable == true, then its safe to call getSeekableStream
     * @return
     */
    public boolean isSeekable() { return false; }

}
