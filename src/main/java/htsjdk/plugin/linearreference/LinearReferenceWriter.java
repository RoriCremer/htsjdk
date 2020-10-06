package htsjdk.plugin.linearreference;

import htsjdk.plugin.HtsWriter;
import htsjdk.samtools.reference.FastaReferenceWriter;

import java.io.OutputStream;

// TODO: reference format has neither a header nor an options factory
public interface LinearReferenceWriter extends HtsWriter<Object, Object, FastaReferenceWriter> {

}
