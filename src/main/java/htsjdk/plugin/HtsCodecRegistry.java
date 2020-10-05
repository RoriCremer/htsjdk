package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.exception.HtsjdkPluginException;
import htsjdk.plugin.reads.ReadsCodecDescriptor;
import htsjdk.plugin.reads.ReadsReader;
import htsjdk.plugin.reads.ReadsWriter;
import htsjdk.plugin.reads.ReadsFormat;
import htsjdk.utils.ValidationUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// TODO: is this really a descriptor registry ? HtsFormatDescriptor ?
// TODO: how can we enable errors as warnings for this code/module only...
// TODO: add a descriptor protocol string for lookup (i.e., htsget, or refget ?)
// TODO: distinguish between FileFormatVersion and codec Version ?
// TODO: one codec per format/version may be insufficient ? What if we want alternative BAMReaders
// that use channels ? How do we handle resolving readers if there are multiple implementations ?
// TODO: if this registry becomes mutable (has ANY public mutator), the it needs to be become a
//  non-singleton (with no statics..)
/**
 * Registry/cache for codec descriptors for discovered codecs.
 */
@SuppressWarnings("rawtypes")
public class HtsCodecRegistry {
    private static final HtsCodecRegistry htsCodecRegistry = new HtsCodecRegistry();
    private static ServiceLoader<HtsCodecDescriptor> serviceLoader = ServiceLoader.load(HtsCodecDescriptor.class);

    private static HtsCodecDescriptorByFormat<ReadsFormat, ReadsReader, ReadsWriter, ReadsCodecDescriptor> readsDescriptors = new HtsCodecDescriptorByFormat<>();

    static {
        discoverCodecs().forEach(htsCodecRegistry::registerCodecDescriptor);
    }

    private static List<HtsCodecDescriptor> discoverCodecs() {
        final Iterable<HtsCodecDescriptor> iterable = () -> serviceLoader.iterator();
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    //TODO: this doesn't belong here
    public static final HtsCodecVersion BAM_DEFAULT_VERSION = new HtsCodecVersion(1, 0,0);

    // minimum number of bytes required to allow any codec to deterministically decide if it can
    // decode a stream
    private static int minSignatureSize = 0;

    private HtsCodecRegistry() {}

    /**
     * Add a codec to the registry
     */
    private void registerCodecDescriptor(final HtsCodecDescriptor codecDescriptor) {
        switch (codecDescriptor.getType()) {
            case ALIGNED_READS:
                readsDescriptors.register(
                        (ReadsCodecDescriptor) codecDescriptor,
                        ((ReadsCodecDescriptor) codecDescriptor).getFormat());
                break;

            case REFERENCE:
            case VARIANTS:
            case FEATURES:
                throw new IllegalArgumentException("Codec type not yet implemented");

            default:
                throw new IllegalArgumentException("Unknown codec type");
        }

        final int minSignatureBytesRequired = codecDescriptor.getFileSignatureSize();
        if (minSignatureBytesRequired < 1) {
            throw new HtsjdkPluginException(
                    String.format("%s: file signature size must be > 0", codecDescriptor.getDisplayName())
            );
        }
        minSignatureSize = Integer.max(minSignatureSize, minSignatureBytesRequired);
    }

    // TODO: this should have a name and contract that reflects that its only looking at the URI
    // TODO: return Optional<ReadsCodec> ?
    // TODO: We dont want this to have to accept all the reader factory arguments, so it should just return the
    //       descriptor ?

    @SuppressWarnings("unchecked")
    public static<T extends HtsReader> T getReadsReader(final IOPath inputPath) {
        //TODO: need to ensure that this looks at the actual stream, since it needs to discriminate
        // based on version (not just the file extension)
        final Optional<ReadsCodecDescriptor> codecDescriptor = readsDescriptors.getCodecDescriptor(inputPath);
        return (T) (codecDescriptor.isPresent() ?
                        codecDescriptor.get().getReader(inputPath) :
                        null);
    }

    //TODO: verify the file extension against the readsFormat type (delegate to the descriptor
    // to see if it likes the extension)

    //TODO: this needs an "auto-upgrade" arg
    // get the newest reads writer for the given file extension
    public static<T extends ReadsWriter> T getReadsWriter(final IOPath outputPath) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        final Optional<ReadsFormat> readsFormat = getReadsFormatForExtension(outputPath);
        if (!readsFormat.isPresent()) {
            throw new IllegalArgumentException(String.format("Can't determine format from extension %s", outputPath));
        }
        final Optional<ReadsCodecDescriptor> codecDescriptor = readsDescriptors.getDescriptorFor(outputPath, readsFormat.get());
        if (!codecDescriptor.isPresent()) {
            throw new IllegalArgumentException(String.format("No codec available for %s", outputPath));
        }
        return (T) (codecDescriptor.isPresent() ?
                codecDescriptor.get().getWriter(outputPath) :
                null);

    }

    // TODO: Where should this live ? Use generic types! (not ReadsFormat)
    public static Optional<ReadsFormat> getReadsFormatForExtension(final IOPath outputPath) {
        final Optional<String> outputExtension = outputPath.getExtension();
        if (!outputExtension.isPresent()) {
            return Optional.empty();
        }
        return FileExtensions.getReadsFormat(outputPath);
    }

    //TODO: verify in the descriptor here that the descriptor selected for the readsFormat matches the
    // extension on this outputPath (which should take precedence ?)
    // TODO: also that the readsFormat matches extension
    public static <T extends ReadsWriter> T getReadsWriter(
            final IOPath outputPath,
            final ReadsFormat readsFormat,
            final HtsCodecVersion codecVersion) {
        final Optional<ReadsCodecDescriptor> codecDescriptor = readsDescriptors.getDescriptorFor(readsFormat, codecVersion);
        return (T) (codecDescriptor.isPresent() ?
                codecDescriptor.get().getWriter(outputPath) :
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
