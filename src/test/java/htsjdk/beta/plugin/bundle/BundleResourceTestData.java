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

    //TODO: change all of these names

    // input resources

    public static final Supplier<BundleResource> inputReadsWithSubType = () -> new IOPathResource(
            READS_FILE,
            BundleResourceType.READS,
            BundleResourceType.READS_BAM);
    public static final Supplier<BundleResource> inputReadsNoSubType = () -> new IOPathResource(
            READS_FILE,
            BundleResourceType.READS);
    public static final Supplier<BundleResource> inputIndexWithSubType = () -> new IOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX,
            BundleResourceType.READS_INDEX_BAI);
    public static final Supplier<BundleResource> inputIndexNoSubType = () -> new IOPathResource(
            READS_INDEX,
            BundleResourceType.INDEX);

}
