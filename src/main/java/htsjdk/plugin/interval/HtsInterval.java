package htsjdk.plugin.interval;

// TODO:
// - Should these have a COORDINATE_POLICY type param that is shared with the actual reader,
//   to distinguish between:
//       0-based half open
//       1-based closed
// - Is there any value in having a "normalized" coordinate system to support interconversion
// - Should this use a type param for "queryName" instead of String ? (QueryInterval uses an int reference index)
// - Is this more a "query" than an interval ? rename to HtsQuery ?
//       its a bit weird to call this an interval since it doesn't cross sorted contigs
// - Should this have a more a generic name/methods ?
//      getQueryName -> getSelector/getLocator
// - Wild cards, i.e., end of reference/contig

public interface HtsInterval {

    String getQueryName();

    long getStart();

    long getEnd();
}
