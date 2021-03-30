package htsjdk.beta.codecs.reads.htsget.htsgetBAMV1_2;

import htsjdk.beta.codecs.reads.htsget.HtsgetBAMCodec;
import htsjdk.io.IOPath;
import htsjdk.beta.plugin.HtsDecoder;
import htsjdk.beta.plugin.HtsRecord;
import htsjdk.beta.plugin.bundle.BundleResourceType;
import htsjdk.beta.plugin.bundle.InputBundle;
import htsjdk.beta.plugin.bundle.InputResource;
import htsjdk.beta.plugin.reads.ReadsDecoderOptions;
import htsjdk.beta.plugin.reads.ReadsFormat;

import java.io.InputStream;
import java.util.Optional;

public class HtsgetBAMCodecV1_2 extends HtsgetBAMCodec {

    @Override
    public HtsDecoder<ReadsFormat, ?, ? extends HtsRecord> getDecoder(final IOPath inputPath, final ReadsDecoderOptions decodeOptions) {
        return new HtsgetBAMDecoderV1_2(inputPath, decodeOptions);
    }

    @Override
    public HtsDecoder<ReadsFormat, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName) {
        return null;
    }

    @Override
    public HtsDecoder<ReadsFormat, ?, ? extends HtsRecord> getDecoder(final InputBundle inputBundle,
                                                                      final ReadsDecoderOptions decodeOptions) {
        final Optional<InputResource> readsResource = inputBundle.get(BundleResourceType.READS);
        if (!readsResource.isPresent()) {
            throw new IllegalArgumentException("The input bundle contains no reads source");
        }
        final Optional<IOPath> inputPath = readsResource.get().getIOPath();
        if (!inputPath.isPresent()) {
            throw new IllegalArgumentException("The reads source must be a IOPath");
        }
        return new HtsgetBAMDecoderV1_2(inputPath.get(), decodeOptions);
    }

    @Override
    public HtsDecoder<ReadsFormat, ?, ? extends HtsRecord> getDecoder(final InputStream is, final String displayName, final ReadsDecoderOptions decodeOptions) {
        return null;
    }
}
