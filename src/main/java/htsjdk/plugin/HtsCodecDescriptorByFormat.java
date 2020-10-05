package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// TODO: one codec per format/version ??

class HtsCodecDescriptorByFormat<FORMAT, READER, WRITER, DESCRIPTOR extends HtsCodecDescriptor<FORMAT, READER, WRITER>> {

    private final Map<FORMAT, Map<HtsCodecVersion, DESCRIPTOR>> descriptors = new HashMap<>();
    private final Map<FORMAT, HtsCodecVersion> newestVersion = new HashMap<>();

    public void register(final DESCRIPTOR descriptor, final FORMAT codecFormatType) {
        descriptors.compute(
                codecFormatType,
                (k, v) -> {
                    final Map<HtsCodecVersion, DESCRIPTOR> versionMap = v == null ? new HashMap<>() : v;
                    versionMap.put(descriptor.getVersion(), descriptor);
                    return versionMap;
                });
        updateNewestVersion(codecFormatType, descriptor.getVersion());
    }

    public Optional<DESCRIPTOR> getCodecDescriptor(final IOPath inputPath) {
        return descriptors.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .filter(descriptor -> descriptor.canDecode(inputPath))
                .findFirst();
    }

    public Optional<DESCRIPTOR> getDescriptorFor(final FORMAT formatType, HtsCodecVersion codecVersion) {
        return descriptors.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .filter(descriptor ->
                        descriptor.getFormat().equals(formatType) &&
                        descriptor.getVersion().equals(codecVersion))
                .findFirst();
    }

    // get the newest (version) descriptor for the given file extension
    public Optional<DESCRIPTOR> getDescriptorFor(final IOPath outputPath, FORMAT format) {
        ValidationUtils.nonNull(outputPath, "Output path must not be null");
        final Optional<HtsCodecVersion> newestFormatVersion = getNewestVersion(format);
        if (!newestFormatVersion.isPresent()) {
            throw new IllegalArgumentException(String.format("No codec versions available for %s", outputPath));
        }
        final Optional<DESCRIPTOR> descriptor = getDescriptorFor(format, newestFormatVersion.get());
        return descriptor;
    }


    public Optional<HtsCodecVersion> getNewestVersion(final FORMAT format) {
        return Optional.of(newestVersion.get(format));
    }

    private void updateNewestVersion(final FORMAT formatForDescriptor, final HtsCodecVersion newVersion) {
        ValidationUtils.nonNull(formatForDescriptor);
        ValidationUtils.nonNull(newVersion);
        newestVersion.compute(
                formatForDescriptor,
                (format, previousVersion) ->
                        previousVersion == null ?
                                newVersion :
                                previousVersion.compareTo(newVersion) > 0 ?
                                        previousVersion :
                                        newVersion);
    }
}
