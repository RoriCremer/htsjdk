package htsjdk.beta.plugin.bundle;

import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;

import java.util.function.Supplier;

public class BundleResourceTestData {
    public final static IOPath READS_FILE = new HtsPath("myreads.bam");
    public final static IOPath READS_INDEX = new HtsPath("myreads.bai");

    public final static class TestHtsPathSubclass extends HtsPath {
        public TestHtsPathSubclass(final String pathString) {
            super(pathString);
        }
    }

    // input resources

    public static final Supplier<InputResource> inputReadsWithSubType = () -> new InputIOPathResource(
            READS_FILE,
            BundleResourceType.READS,
            BundleResourceType.READS_BAM);
    public static final Supplier<InputResource> inputReadsNoSubType = () -> new InputIOPathResource(
            READS_FILE,
            BundleResourceType.READS);
    public static final Supplier<InputResource> inputIndexWithSubType = () -> new InputIOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX,
            BundleResourceType.READS_INDEX_BAI);
    public static final Supplier<InputResource> inputIndexNoSubType = () -> new InputIOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX);

    // output resources

    public static final Supplier<OutputResource> outputReadsWithSubType = () -> new OutputIOPathResource(
            READS_FILE,
            BundleResourceType.READS,
            BundleResourceType.READS_BAM);
    public static final Supplier<OutputResource> outputReadsNoSubType = () -> new OutputIOPathResource(
            READS_FILE,
            BundleResourceType.READS);
    public static final Supplier<OutputResource> outputIndexWithSubType = () -> new OutputIOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX,
            BundleResourceType.READS_INDEX_BAI);
    public static final Supplier<OutputResource> outputIndexNoSubType = () -> new OutputIOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX);
}
