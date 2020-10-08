package htsjdk.plugin.hapref;

import htsjdk.plugin.HtsReader;
import htsjdk.samtools.reference.ReferenceSequenceFile;

// TODO: reference format has neither a header nor an options factory
public interface HaploidReferenceReader extends HtsReader<Object, Object, ReferenceSequenceFile> {

}
