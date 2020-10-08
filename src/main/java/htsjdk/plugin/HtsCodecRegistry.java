package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.exception.HtsjdkPluginException;
import htsjdk.plugin.hapref.HaploidReferenceCodec;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.plugin.hapref.HaploidReferenceReader;
import htsjdk.plugin.hapref.HaploidReferenceWriter;
import htsjdk.plugin.reads.ReadsCodec;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;
import htsjdk.utils.ValidationUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// TODO: how can we enable errors as warnings for this code/module only...
// TODO: add a codec protocol string for lookup (i.e., htsget, or refget ?)
// TODO: distinguish between FileFormatVersion and codec Version ?
// TODO: if this registry becomes mutable (has ANY public mutator), the it needs to be become a
//  non-singleton (with no statics..)
// TODO: where does an upgrade happen ? Who bridges the version incompatibilities ?
// TODO: need a resource collection to represent siblings (resource, index, dict)

// TODO: does htsget have  version # embedded in the stream
/**
 * Registry/cache for discovered codecs.
 */
@SuppressWarnings("rawtypes")
public class HtsCodecRegistry {
    private static final HtsCodecRegistry htsCodecRegistry = new HtsCodecRegistry();
    private static ServiceLoader<HtsCodec> serviceLoader = ServiceLoader.load(HtsCodec.class);

    private static HtsCodecs<ReadsFormat, ReadsReader, ReadsWriter, ReadsCodec>
            readsCodecs = new HtsCodecs<>();
    private static HtsCodecs<HaploidReferenceFormat, HaploidReferenceReader, HaploidReferenceWriter, HaploidReferenceCodec>
            hapRefCodecs = new HtsCodecs<>();

    static {
        discoverCodecs().forEach(htsCodecRegistry::registerCodec);
    }

    private static List<HtsCodec> discoverCodecs() {
        final Iterable<HtsCodec> iterable = () -> serviceLoader.iterator();
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    // minimum number of bytes required to allow any codec to deterministically decide if it can
    // decode a stream
    private static int minSignatureSize = 0;

    private HtsCodecRegistry() {}

    /**
     * Add a codec to the registry
     */
    private void registerCodec(final HtsCodec codec) {
        switch (codec.getType()) {
            case ALIGNED_READS:
                readsCodecs.register(((ReadsCodec) codec).getFormat(), (ReadsCodec) codec);
                break;

            case REFERENCE:
                hapRefCodecs.register(((HaploidReferenceCodec) codec).getFormat(), (HaploidReferenceCodec) codec);
                break;

            case VARIANTS:
            case FEATURES:
                throw new IllegalArgumentException("Codec type not yet implemented");

            default:
                throw new IllegalArgumentException("Unknown codec type");
        }

        final int minSignatureBytesRequired = codec.getSignatureSize();
        if (minSignatureBytesRequired < 1) {
            throw new HtsjdkPluginException(
                    String.format("%s: file signature size must be > 0", codec.getDisplayName())
            );
        }
        minSignatureSize = Integer.max(minSignatureSize, minSignatureBytesRequired);
    }

    // TODO: this should have a name and contract that reflects that its only looking at the URI
    // TODO: return Optional<ReadsCodec> ?
    // TODO: We dont want this to have to accept all the reader factory arguments, so it should just return the
    //       codec ?
    //TODO: these should catch/transform ClassCastException
    //TODO: these need to throw rather than returning null
    @SuppressWarnings("unchecked")
    public static<T extends HtsReader> T getReadsReader(final IOPath inputPath) {
        //TODO: need to ensure that this looks at the actual stream, since it needs to discriminate
        // based on version (not just the file extension)
        final Optional<ReadsCodec> codec = readsCodecs.getCodecForIOPath(inputPath);
        return (T) (codec.isPresent() ?
                codec.get().getReader(inputPath) :
                null);
    }

    public static<T extends HtsReader> T getReferenceReader(final IOPath inputPath) {
        //TODO: need to ensure that this looks at the actual stream, since it needs to discriminate
        // based on version (not just the file extension)
        final Optional<HaploidReferenceCodec> codec = hapRefCodecs.getCodecForIOPath(inputPath);
        return (T) (codec.isPresent() ?
                codec.get().getReader(inputPath) :
                null);
    }

    //TODO: verify the file extension against the readsFormat type (delegate to the codec
    // to see if it likes the extension)
    //TODO: this needs an "auto-upgrade" arg
    // get the newest reads writer for the given file extension
    public static<T extends ReadsWriter> T getReadsWriter(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        final Optional<ReadsCodec> codec = readsCodecs.getCodecForIOPath(outputPath);
        if (!codec.isPresent()) {
            throw new IllegalArgumentException(String.format("No codec available for %s", outputPath));
        }
        return (T) (codec.isPresent() ?
                codec.get().getWriter(outputPath) :
                null);
    }

    //TODO: verify in the codec here that the codec selected for the readsFormat matches the
    // extension on this outputPath (which should take precedence ?)
    // TODO: also that the readsFormat matches extension
    public static <T extends ReadsWriter> T getReadsWriter(
            final IOPath outputPath,
            final ReadsFormat readsFormat,
            final HtsCodecVersion codecVersion) {
        final Optional<ReadsCodec> codec = readsCodecs.getCodecForFormatAndVersion(readsFormat, codecVersion);
        return (T) (codec.isPresent() ?
                codec.get().getWriter(outputPath) :
                null);
    }

//    // Once we find a codec, hand it off already primed with the version header, etc).
//    public static ReadsCodec findReadsCodecFor(final String sourceName, final InputStream is) {
//        final byte[] signatureBytes = new byte[minSignatureSize];
//        try {
//            final int numRead = is.read(signatureBytes);
//            if (numRead <= 0) {
//                throw new HtsjdkIOException(String.format("Failure reading content from stream for %s", sourceName));
//            }
//            return discoveredCodecs
//                    .values()
//                    .stream()
//                    .flatMap(m -> m.values().stream())
//                    .filter(
//                        // its possible that the input is a legitimate stream for some codec, but
//                        // contains less bytes than are required even for signature detection by another
//                        // codecs, so skip any descriptors that require more bytes than are available
//                        codecDescriptor ->
//                                numRead >= codecDescriptor.getFileSignatureSize() &&
//                                codecDescriptor.canDecode(signatureBytes))
//                    .findFirst()
//                    .orElseThrow(() -> new HtsjdkIOException(String.format("No codec found for %s", sourceName)))
//                    .getCodecInstance(is);
//        } catch (IOException e) {
//            throw new HtsjdkIOException(String.format("Failure reading signature from stream for %s", sourceName), e);
//        }
//    }
}
