package htsjdk.beta.plugin.reads;

import htsjdk.beta.plugin.IOUtils;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import htsjdk.beta.plugin.bundle.BundleResourceType;
import htsjdk.beta.plugin.bundle.Bundle;
import htsjdk.beta.plugin.bundle.IOPathResource;
import htsjdk.beta.plugin.bundle.BundleResource;
import htsjdk.samtools.SamFiles;
import htsjdk.samtools.util.FileExtensions;
import htsjdk.samtools.util.Log;
import htsjdk.samtools.util.Tuple;
import htsjdk.utils.ValidationUtils;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A reads bundle has a primary resource with content type READS; an optional index resource.
 */
public class ReadsBundle<T extends IOPath> extends Bundle implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Log LOG = Log.getInstance(ReadsBundle.class);
    public static final String READS_MISSING = "No reads in reads bundle";

    private final T cachedReadsPath;

    public ReadsBundle(final T reads) {
        super(BundleResourceType.READS,
                Collections.singletonList(
                        toInputResource(
                                BundleResourceType.READS,
                                ValidationUtils.nonNull(reads, "reads"))));
        validateContentTypes(BundleResourceType.READS, reads);
        cachedReadsPath = getReadsResourceOrThrow();
    }

    public ReadsBundle(final T reads, final T index) {
        super(BundleResourceType.READS,
                Arrays.asList(
                        toInputResource(BundleResourceType.READS, ValidationUtils.nonNull(reads, "reads")),
                        toInputResource(BundleResourceType.INDEX, ValidationUtils.nonNull(index, "index"))));
        validateContentTypes(BundleResourceType.READS, reads);
        cachedReadsPath = getReadsResourceOrThrow();
    }

    /**
     * A reads bundle can be constructed from a JSON string as long as the bundle has a primary resource with
     * content type "READS". It may also have an optional index resource.
     */
    public ReadsBundle(final String jsonString) {
        this(jsonString, HtsPath::new);
    }

    public ReadsBundle(final String jsonString, final Function<String, IOPath> customPathConstructor) {
        super(jsonString, customPathConstructor);
        cachedReadsPath = getReadsResourceOrThrow();
        validateContentTypes(BundleResourceType.READS, cachedReadsPath);
    }

    public T getReads() { return cachedReadsPath; }

    public Optional<T> getIndex() {
        final Supplier<RuntimeException> ex = () -> new RuntimeException("Index resource is present with a null path");
        final Optional<BundleResource> inputResource = get(BundleResourceType.INDEX);
        // its OK for there to be no index resource, but if there *is* an index resource, it must contain
        // a non-null path...
        return inputResource.isPresent() ?
                Optional.of((T) inputResource.get().getIOPath().orElseThrow(ex)) :
                Optional.empty();
    }

    public static <T extends IOPath> ReadsBundle getReadsBundleFromPath(final T jsonPath) {
        return new ReadsBundle(IOUtils.getStringFromPath(jsonPath));
    }

    public static <T extends IOPath> ReadsBundle resolveIndex(T reads) {
        final Path index = SamFiles.findIndex(reads.toPath());
        if (index == null) {
            return new ReadsBundle(reads);
        }
        return new ReadsBundle(reads, IOUtils.toHtsPath(index));
    }

    public static <T extends IOPath> boolean looksLikeAReadsBundle(final T rawReadPath) {
        return rawReadPath.getURI().getPath().endsWith(BUNDLE_EXTENSION);
    }

    // try to get the reads for the side effect of validating that the bundle actually contains
    // a reads resource. throw if its not present
    private T getReadsResourceOrThrow() {
        final Supplier<RuntimeException> ex = () -> new RuntimeException("No reads in reads bundle");
        final Optional<IOPath> ioPath = get(BundleResourceType.READS).orElseThrow(ex).getIOPath();
        return (T) ioPath.orElseThrow(ex);
    }

    private static <T extends IOPath> IOPathResource toInputResource(final String providedContentType, final T ioPath) {
        ValidationUtils.nonNull(ioPath, "ioPath");
        final Optional<Tuple<String, String>> typePair = getInferredContentTypes(ioPath);
        if (typePair.isPresent()) {
            if (providedContentType != null && !typePair.get().a.equals(providedContentType)) {
                LOG.warn(String.format(
                        "Provided content type \"%s\" for \"%s\" doesn't match derived content type \"%s\"",
                        providedContentType,
                        ioPath.getRawInputString(),
                        typePair.get().a));
            }
            return new IOPathResource(
                    ioPath,
                    providedContentType,  // prefer the provided content type
                    typePair.get().b);
        } else {
            return new IOPathResource(
                    ioPath,
                    providedContentType);
        }
    }

    //TODO: is it worth trying to infer the content type from the IOPath, and warning if it doesn't look
    // like a legitimate reads source?
    //get the inferred contentType/subType from an IOPath, ie., READS/BAM
    private static <T extends IOPath> Optional<Tuple<String, String>> getInferredContentTypes(final T ioPath) {
        ValidationUtils.nonNull(ioPath, "ioPath");
        final Optional<String> extension = ioPath.getExtension();
        if (extension.isPresent()) {
            final String ext = extension.get();
            if (ext.equals(FileExtensions.BAM)) {
                return Optional.of(new Tuple<>(BundleResourceType.READS, BundleResourceType.READS_BAM));
            } else if (ext.equals(FileExtensions.CRAM)) {
                return Optional.of(new Tuple<>(BundleResourceType.READS, BundleResourceType.READS_CRAM));
            } else if (ext.equals((FileExtensions.SAM))) {
                return Optional.of(new Tuple<>(BundleResourceType.READS, BundleResourceType.READS_SAM));
            }
            //TODO: else SRA, htsget,...
        }
        return Optional.empty();
    }

    private static <T extends IOPath> boolean validateContentTypes(final String expectedContentType, final T ioPath) {
        final Optional<Tuple<String, String>> inferredType = getInferredContentTypes(ioPath);
        return inferredType.isPresent() ?
                inferredType.get().a.equals(expectedContentType) :
                false;
    }
}
