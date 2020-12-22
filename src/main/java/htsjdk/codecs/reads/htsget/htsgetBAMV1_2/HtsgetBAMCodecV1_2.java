package htsjdk.codecs.reads.htsget.htsgetBAMV1_2;

import htsjdk.codecs.reads.htsget.HtsgetBAMCodec;
import htsjdk.io.IOPath;
import htsjdk.plugin.HtsDecoder;
import htsjdk.plugin.HtsRecord;
import htsjdk.plugin.bundle.BundleResourceType;
import htsjdk.plugin.bundle.InputBundle;
import htsjdk.plugin.bundle.InputResource;
import htsjdk.plugin.reads.ReadsDecoderOptions;
import htsjdk.plugin.reads.ReadsFormat;

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
