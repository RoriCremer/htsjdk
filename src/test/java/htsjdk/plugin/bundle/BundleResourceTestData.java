package htsjdk.plugin.bundle;

import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;

import java.util.HashMap;
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
    public static final Supplier<InputResource> inputReadsWithTag = () -> new InputIOPathResource(
            new HtsPath("my.bam"),
            BundleResourceType.READS,
            BundleResourceType.READS_BAM,
            "testTAG");
    public static final Supplier<InputResource> inputReadsWithTagOneAttribute = () -> new InputIOPathResource(
                new HtsPath("my.bam"),
                BundleResourceType.READS,
                BundleResourceType.READS_BAM,
            "testTAG",
                new HashMap<String, String>() {{
                    put("attribute1", "value1");
                }});
    public static final Supplier<InputResource> inputReadsWithTagTwoAttributes = () -> new InputIOPathResource(
            new HtsPath("my.bam"),
            BundleResourceType.READS,
            BundleResourceType.READS_BAM,
            "testTAG",
            new HashMap<String, String>() {{
                put("attribute1", "value1");
                put("attribute2", "value2");
            }});
    public static final Supplier<InputResource> inputReadsNoTagTwoAttributes = () -> new InputIOPathResource(
            new HtsPath("my.bam"),
            BundleResourceType.READS,
            BundleResourceType.READS_BAM,
            null,
            new HashMap<String, String>() {{
                put("attribute1", "value1");
                put("attribute2", "value2");
            }});

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
    public static final Supplier<OutputResource> outputReadsWithTag = () -> new OutputIOPathResource(
            new HtsPath("my.bam"),
            BundleResourceType.READS,
            BundleResourceType.READS_BAM,
            "testTAG");
    public static final Supplier<OutputResource> outputReadsWithTagOneAttribute = () -> new OutputIOPathResource(
            new HtsPath("my.bam"),
            BundleResourceType.READS,
            BundleResourceType.READS_BAM,
            "testTAG",
            new HashMap<String, String>() {{
                put("attribute1", "value1");
            }});
    public static final Supplier<OutputResource> outputReadsWithTagTwoAttributes = () -> new OutputIOPathResource(
            new HtsPath("my.bam"),
            BundleResourceType.READS,
            BundleResourceType.READS_BAM,
            "testTAG",
            new HashMap<String, String>() {{
                put("attribute1", "value1");
                put("attribute2", "value2");
            }});
    public static final Supplier<OutputResource> outputReadsNoTagTwoAttributes = () -> new OutputIOPathResource(
            new HtsPath("my.bam"),
            BundleResourceType.READS,
            BundleResourceType.READS_BAM,
            null,
            new HashMap<String, String>() {{
                put("attribute1", "value1");
                put("attribute2", "value2");
            }});

}
