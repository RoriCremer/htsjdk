package htsjdk.plugin.bundle;

//import java.util.HashSet;

/**
 * Defines a namespace for a bunch of constants to identify the resources in a {@link Bundle}.
 */
public class BundleResourceType {

    public static final String READS = "READS";

    public static final String REFERENCE = "REFERENCE";
    public static final String DICTIONARY = "DICTIONARY";

    public static final String VARIANTS = "VARIANTS";
    public static final String FEATURES = "FEATURES";

    //TODO: resolve this into a set of more specific types (BAI, CSI, IDX, TBI..) ?
    public static final String INDEX = "INDEX";
    public static final String MD5 = "MD5";

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
