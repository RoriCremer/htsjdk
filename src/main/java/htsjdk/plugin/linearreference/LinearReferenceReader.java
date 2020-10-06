package htsjdk.plugin.linearreference;

import htsjdk.plugin.HtsReader;
import htsjdk.samtools.reference.ReferenceSequenceFile;

// TODO: reference format has neither a header nor an options factory
public interface LinearReferenceReader extends HtsReader<Object, Object, ReferenceSequenceFile> {

}
