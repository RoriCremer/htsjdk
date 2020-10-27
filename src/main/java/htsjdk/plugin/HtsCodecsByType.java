package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: need more than one codec per format/version, ie., we'll need an HtsGetCodec for BAM v1 that
// uses the same encoder/decoder as the BAMv1Codec

final class HtsCodecsByType<FORMAT, CODEC extends HtsCodec<FORMAT, ?, ?>> {

    private final Map<FORMAT, Map<HtsCodecVersion, CODEC>> codecs = new HashMap<>();
    private final Map<FORMAT, HtsCodecVersion> newestVersion = new HashMap<>();

    public void register(final CODEC codec) {
        final FORMAT codecFormatType = codec.getFileFormat();
        codecs.compute(
                codecFormatType,
                (k, v) -> {
                    final Map<HtsCodecVersion, CODEC> versionMap = v == null ? new HashMap<>() : v;
                    versionMap.put(codec.getVersion(), codec);
                    return versionMap;
                });
        updateNewestVersion(codecFormatType, codec.getVersion());
    }

    //TODO: check for/handle > 1 matches ?
    public Optional<CODEC> getCodecForIOPath(final IOPath inputPath) {
        return getCodecsForIOPath(inputPath)
                .stream()
                .findFirst();
    }

    //TODO: check for/handle > 1 matches ?
    public List<CODEC> getCodecsForIOPath(final IOPath inputPath) {
        return codecs.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .filter(codec -> codec.canDecodeExtension(inputPath))
                .collect(Collectors.toList());
    }

    public Optional<CODEC> getCodecForFormatAndVersion(final FORMAT formatType, HtsCodecVersion codecVersion) {
        return codecs.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .filter(codec -> codec.getFileFormat().equals(formatType) && codec.getVersion().equals(codecVersion))
                .findFirst();
    }

    // get the newest version codec for the given file extension
    public Optional<CODEC> getCodecForFormatAndVersion(final IOPath outputPath, FORMAT format) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        final Optional<HtsCodecVersion> newestFormatVersion = getNewestVersion(format);
        if (!newestFormatVersion.isPresent()) {
            throw new IllegalArgumentException(String.format("No codec versions available for %s", outputPath));
        }
        final Optional<CODEC> codec = getCodecForFormatAndVersion(format, newestFormatVersion.get());
        return codec;
    }

    public Optional<HtsCodecVersion> getNewestVersion(final FORMAT format) {
        return Optional.of(newestVersion.get(format));
    }

    private void updateNewestVersion(final FORMAT codecFormat, final HtsCodecVersion newVersion) {
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
