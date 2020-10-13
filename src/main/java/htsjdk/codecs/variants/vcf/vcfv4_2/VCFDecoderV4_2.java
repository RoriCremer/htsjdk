package htsjdk.codecs.variants.vcf.vcfv4_2;

import htsjdk.codecs.variants.vcf.VCFDecoder;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsCodecVersion;
import htsjdk.plugin.variants.VariantsDecoderOptions;
import htsjdk.utils.ValidationUtils;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFReader;

import java.io.IOException;
import java.io.InputStream;

public class VCFDecoderV4_2 extends VCFDecoder {
    private final VCFReader vcfReader;
    private final VCFHeader vcfHeader;

    public VCFDecoderV4_2(final IOPath inputPath) {
        this(inputPath, new VariantsDecoderOptions());
    }

    public VCFDecoderV4_2(final IOPath inputPath, final VariantsDecoderOptions decoderOptions) {
        super(inputPath);
        ValidationUtils.nonNull(decoderOptions);
        vcfReader = getVCFReader(decoderOptions);
        vcfHeader = vcfReader.getHeader();
    }

    public VCFDecoderV4_2(final InputStream is, final String displayName) {
        this(is, displayName, new VariantsDecoderOptions());
    }

    public VCFDecoderV4_2(final InputStream is, final String displayName, final VariantsDecoderOptions decoderOptions) {
        super(is, displayName);
        ValidationUtils.nonNull(decoderOptions);
        vcfReader = getVCFReader(decoderOptions);
        vcfHeader = vcfReader.getHeader();
    }

    @Override
    public HtsCodecVersion getVersion() {
        return VCFCodecV4_2.VCF_V42_VERSION;
    }

    @Override
    public VCFReader getRecordReader() {
        return vcfReader;
    }

    @Override
    public VCFHeader getHeader() {
        return vcfHeader;
    }

    @Override
    public void close() {
        try {
            vcfReader.close();
        } catch (IOException e) {
            throw new HtsjdkIOException(String.format("Exception closing input stream %s for", inputPath), e);
        }
    }

    private VCFReader getVCFReader(final VariantsDecoderOptions decoderOptions) {
        if (is != null) {
            throw new IllegalArgumentException("VCF reader from stream not implemented");
        } else {
            return new VCFFileReader(inputPath.toPath(),false);
        }
    }
}
