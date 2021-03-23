package htsjdk.plugin.bundle;

//import java.util.HashSet;

/**
 * Defines a namespace for a bunch of constants to identify the resources in a {@link Bundle}.
 */
public class BundleResourceType {

    // content types
    public static final String READS = "READS";
    public static final String REFERENCE = "REFERENCE";
    public static final String DICTIONARY = "DICTIONARY";
    public static final String VARIANTS = "VARIANTS";
    public static final String FEATURES = "FEATURES";
    public static final String INDEX = "INDEX";

    // content subtypes
    public static final String SUB_TYPE_UNKNOWN = "UNKNOWN";

    // content subtypes - reads
    public static final String READS_SAM = "SAM";
    public static final String READS_BAM = "BAM";
    public static final String READS_CRAM = "CRAM";
    public static final String READS_SRA = "SRA";
    public static final String READS_HTSGET_BAM = "HTSGET_BAM";
    public static final String READS_HTSGET_SAM = "HTSGET_SAM";

    // content subtypes - reads index
    public static final String READS_INDEX_BAI = "BAI";
    public static final String READS_INDEX_CRAI = "CRAI";
    public static final String READS_INDEX_CSI = "CSI";
    public static final String READS_INDEX_SELF_INDEXING = "SELF";


// Method to determine if a dynamic content type string clashes with any built-in string
//
//    public static HashSet<String> predefinedTypes = new HashSet() {{
//        add(READS);
//        add(REFERENCE);
//        add(DICTIONARY);
//        add(VARIANTS);
//        add(FEATURES);
//        add(INDEX);
//        add(MD5);
//    }};
//
//    // determine if a custom type clashes with an existing type
//    public static boolean isPredefinedResourceType(final String target) { return predefinedTypes.contains(target);  }
}
