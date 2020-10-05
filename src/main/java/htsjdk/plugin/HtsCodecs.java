package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// TODO: need more than one codec per format/version, ie., we'll need an HtsGetCodec for BAM v1 that
// uses the same reader/writer as the BAMv1Codec

class HtsCodecs<FORMAT, READER extends HtsReader, WRITER extends HtsWriter, CODEC extends HtsCodec<FORMAT, READER, WRITER>> {

    private final Map<FORMAT, Map<HtsCodecVersion, CODEC>> codecs = new HashMap<>();
    private final Map<FORMAT, HtsCodecVersion> newestVersion = new HashMap<>();

    public void register(final FORMAT codecFormatType, final CODEC codec) {
        codecs.compute(
                codecFormatType,
                (k, v) -> {
                    final Map<HtsCodecVersion, CODEC> versionMap = v == null ? new HashMap<>() : v;
                    versionMap.put(codec.getVersion(), codec);
                    return versionMap;
                });
        updateNewestVersion(codecFormatType, codec.getVersion());
    }

    public Optional<CODEC> getCodec(final IOPath inputPath) {
        return codecs.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .filter(codec -> codec.canDecode(inputPath))
                .findFirst();
    }

    public Optional<CODEC> getCodec(final FORMAT formatType, HtsCodecVersion codecVersion) {
        return codecs.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .filter(codec ->
                        codec.getFormat().equals(formatType) &&
                                codec.getVersion().equals(codecVersion))
                .findFirst();
    }

    // get the newest (version) codec for the given file extension
    public Optional<CODEC> getCodec(final IOPath outputPath, FORMAT format) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        final Optional<HtsCodecVersion> newestFormatVersion = getNewestVersion(format);
        if (!newestFormatVersion.isPresent()) {
            throw new IllegalArgumentException(String.format("No codec versions available for %s", outputPath));
        }
        final Optional<CODEC> codec = getCodec(format, newestFormatVersion.get());
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
