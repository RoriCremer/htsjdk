package htsjdk.codecs.linearreference.fastareference;

import htsjdk.codecs.linearreference.LinearFASTACodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.linearreference.LinearReferenceFormat;
import htsjdk.plugin.linearreference.LinearReferenceReader;
import htsjdk.plugin.linearreference.LinearReferenceWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public abstract class FastaCodec extends LinearFASTACodec {

    public static final HtsCodecVersion VERSION_1 = new HtsCodecVersion(1, 0, 0);

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_1;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public LinearReferenceFormat getFormat() {
        return LinearReferenceFormat.FASTA;
    }

    @Override
    public int getFileSignatureSize() {
        return 0;
    }

    @Override
    public boolean canDecode(IOPath resource) {
        return false;
    }

    @Override
    public boolean canDecode(Path path) {
        return false;
    }

    @Override
    public boolean canDecode(byte[] streamSignature) {
        return false;
    }

    @Override
    public LinearReferenceReader getReader(InputStream is, String displayName) {
        return null;
    }

    @Override
    public LinearReferenceWriter getWriter(OutputStream os, String displayName) {
        return null;
    }

    @Override
    public boolean runVersionUpgrade(HtsCodecVersion sourceCodecVersion, HtsCodecVersion targetCodecVersion) {
        return false;
    }
}
