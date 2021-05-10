package htsjdk.beta.plugin.registry;

import java.io.ByteArrayInputStream;

//TODO: this wrapper class may wind up being entirely unnecessary (ByteArrayInputStream could
// be used directly), although it does act as a sentinel to signal that the stream is being
// used for signature probing.

/**
 * An input stream over the first N bytes of another input stream, used to allow multiple
 * codecs to probe those bytes for a file format/version signature.
 */
public class SignatureProbingInputStream extends ByteArrayInputStream {
    final int signaturePrefixSize;

    public SignatureProbingInputStream(final byte[] signaturePrefix, final int signaturePrefixSize) {
        super(signaturePrefix);
        this.signaturePrefixSize = signaturePrefixSize;
    }

}
