package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Class used by the registry to track all codecs for a given category.
// TODO: need more than one codec per format/version, ie., we'll need an HtsGetCodec for BAM v1 that
// uses the same encoder/decoder as the BAMv1Codec

/**
 * Class used by the registry to track all codec formats and versions for a single codec type.
 * @param <F> enum representing the formats for this codec type
 * @param <C> the codec type
 */
final class HtsVersionsByFormat<F extends Enum<F>, C extends HtsCodec<F, ?, ?>> {

    private final Map<F, Map<HtsCodecVersion, C>> codecs = new HashMap<>();
    private final Map<F, HtsCodecVersion> newestVersion = new HashMap<>();

    public void register(final C codec) {
        final F codecFormatType = codec.getFileFormat();
        codecs.compute(
                codecFormatType,
                (k, v) -> {
                    final Map<HtsCodecVersion, C> versionMap = v == null ? new HashMap<>() : v;
                    versionMap.put(codec.getVersion(), codec);
                    return versionMap;
                });
        updateNewestVersion(codecFormatType, codec.getVersion());
    }

    //TODO: check for/handle > 1 matches ?
    public Optional<C> getCodecForIOPath(final IOPath inputPath) {
        return getCodecsForIOPath(inputPath)
                .stream()
                .findFirst();
    }

    //TODO: check for/handle > 1 matches ?
    public List<C> getCodecsForIOPath(final IOPath inputPath) {
        return codecs.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .filter(codec -> codec.canDecodeURI(inputPath))
                .collect(Collectors.toList());
    }

    public Optional<C> getCodecForFormatAndVersion(final F formatType, HtsCodecVersion codecVersion) {
        return codecs.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .filter(codec -> codec.getFileFormat().equals(formatType) && codec.getVersion().equals(codecVersion))
                .findFirst();
    }

    // get the newest version codec for the given file extension
    public Optional<C> getCodecForFormatAndVersion(final IOPath outputPath, F format) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        final Optional<HtsCodecVersion> newestFormatVersion = getNewestVersion(format);
        //TODO: this returns Optional so just return Optional.empty
        if (!newestFormatVersion.isPresent()) {
            throw new IllegalArgumentException(String.format("No codec versions available for %s", outputPath));
        }
        final Optional<C> codec = getCodecForFormatAndVersion(format, newestFormatVersion.get());
        return codec;
    }

    public Optional<HtsCodecVersion> getNewestVersion(final F format) {
        return Optional.of(newestVersion.get(format));
    }

    private void updateNewestVersion(final F codecFormat, final HtsCodecVersion newVersion) {
        ValidationUtils.nonNull(codecFormat);
        ValidationUtils.nonNull(newVersion);
        newestVersion.compute(
                codecFormat,
                (format, previousVersion) ->
                        previousVersion == null ?
                                newVersion :
                                previousVersion.compareTo(newVersion) > 0 ?
                                        previousVersion :
                                        newVersion);
    }
}
