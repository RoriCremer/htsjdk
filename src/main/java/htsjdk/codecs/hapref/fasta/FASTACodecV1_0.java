package htsjdk.codecs.hapref.fasta;

import htsjdk.codecs.hapref.HapRefCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.hapref.HaploidReferenceFormat;
import htsjdk.plugin.hapref.HaploidReferenceReader;
import htsjdk.plugin.hapref.HaploidReferenceWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class FASTACodecV1_0 extends HapRefCodec {

    public static final HtsCodecVersion VERSION_1 = new HtsCodecVersion(1, 0, 0);

    @Override
    public HtsCodecVersion getVersion() {
        return VERSION_1;
    }

    @Override
    public HaploidReferenceFormat getFormat() {
        return HaploidReferenceFormat.FASTA;
    }

    @Override
    public int getFileSignatureSize() {
        return 1;
    }

    @Override
    public boolean canDecode(IOPath ioPath) {
        return ioPath.hasExtension(".fasta");
    }

    @Override
    public boolean canDecode(Path path) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public boolean canDecode(byte[] streamSignature) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HaploidReferenceReader getReader(IOPath inputPath) {
        return new FASTAReaderV1_0(inputPath);
    }

    @Override
    public HaploidReferenceReader getReader(InputStream is, String displayName) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public HaploidReferenceWriter getWriter(IOPath outputPath) {
        return new FASTAWriterV1_0(outputPath);
    }

    @Override
    public HaploidReferenceWriter getWriter(OutputStream os, String displayName) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public boolean runVersionUpgrade(HtsCodecVersion sourceCodecVersion, HtsCodecVersion targetCodecVersion) {
        throw new IllegalStateException("Not implemented");
    }
}
