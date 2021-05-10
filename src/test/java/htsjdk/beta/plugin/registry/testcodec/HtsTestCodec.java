package htsjdk.beta.plugin.registry.testcodec;

import htsjdk.beta.plugin.HtsCodec;
import htsjdk.beta.plugin.HtsCodecType;
import htsjdk.beta.plugin.HtsCodecVersion;
import htsjdk.beta.plugin.bundle.Bundle;
import htsjdk.exception.HtsjdkIOException;
import htsjdk.io.IOPath;
import htsjdk.samtools.util.BlockCompressedInputStream;
import htsjdk.utils.ValidationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

// NOTE: This codec has configurable parameters that allow the tests to instantiate several variations
// of codecs for a fictional file format, with different sub content types, versions, protocol schemes,
// and stream signatures.
//
// Its ok for it to be dynamically discovered when running in a test configuration, but it should
// never be discovered/included at runtime.
//
public class HtsTestCodec implements HtsCodec<
        HtsTestCodecFormat,
        HtsTestDecoderOptions,
        HtsTestEncoderOptions>
{
    private final HtsCodecVersion htsVersion;
    private final HtsTestCodecFormat htsFormat;
    private final  String contentSubType;
    private final  String fileExtension;
    private final  String streamSignature;
    private final  String protocolScheme;
    private final boolean useGzippedInputs;

    public HtsTestCodec() {
        // This codec is for registry testing only, and should only be instantiated by test code
        // using the other (non-standard) configuration constructor. Since this no-arg constructor
        // is the one that will be called if this codec is instantiated through normal dynamic codec
        // discovery, throw if it ever gets called.
        throw new RuntimeException("This codec should never be instantiated using the no-arg constructor");
    }

    // used by tests to create a variety of different test codecs that vary by format/version/extensions/protocol
    public HtsTestCodec(
            final HtsTestCodecFormat htsFormat,
            final HtsCodecVersion htsVersion,
            final String contentSubType,
            final String fileExtension,
            final String streamSignature,
            final String protocolScheme,
            final boolean useGzippedInputs
    ) {
        this.htsFormat              = htsFormat;
        this.htsVersion             = htsVersion;
        this.contentSubType         = contentSubType;
        this.fileExtension          = fileExtension;
        this.streamSignature        = streamSignature;
        this.protocolScheme         = protocolScheme;
        this.useGzippedInputs       = useGzippedInputs;
    }

    @Override
    public HtsCodecType getCodecType() {
        //this isn't really an ALIGNED_READS codec, but we HAVE to use a value from the HtsCodecType enum
        return HtsCodecType.ALIGNED_READS;
    }

    @Override
    public HtsTestCodecFormat getFileFormat() {
        return htsFormat;
    }

    @Override
    public HtsCodecVersion getVersion() {
        return htsVersion;
    }

    @Override
    public int getMinimumStreamDecodeSize() {
        //TODO: fix this
        return 64 * 1024;
    }

    @Override
    public int getSignatureSize() {
        return streamSignature.length() + htsVersion.toString().length();
    }

    @Override
    public boolean claimCustomURI(final IOPath ioPath) {
        return protocolScheme != null && protocolScheme.equals(ioPath.getScheme());
    }

   @Override
    public boolean canDecodeURI(IOPath resource) {
        final Optional<String> extension = resource.getExtension();
        return extension.isPresent() && extension.get().equals(fileExtension);
    }

    @Override
    public boolean canDecodeExtension(Path path) {
        return false;
    }

    //TODO: uses a stream over a byte array rather than the underlying stream to reduce the need to
    // repeatedly mark/reset the stream for each codec ?
    //TODO: document to NEVER close this stream
    @Override
    public boolean canDecodeSignature(final InputStream rawInputStream, final String sourceName) {
        ValidationUtils.nonNull(rawInputStream);
        ValidationUtils.nonNull(sourceName);

        try {
            final int signatureSize = getSignatureSize();
            final byte[] signatureBytes = new byte[signatureSize];

            if (useGzippedInputs) {
                // first, probe to see if it looks gzipped
                final boolean isBlockCompressed = BlockCompressedInputStream.isValidFile(rawInputStream);
                rawInputStream.reset();
                if (!isBlockCompressed) {
                    return false; // this codec requires gzipped input but this input isn't gzipped
                }
            }
            //TODO: need a try/catch block ? WAIT. Never close this stream. NEVER.
            final InputStream streamToUse = useGzippedInputs ? new BlockCompressedInputStream(rawInputStream) : rawInputStream;
            int numRead = streamToUse.read(signatureBytes);
            if (numRead <= 0) {
                throw new HtsjdkIOException(String.format("Failure reading content from input stream for %s", sourceName));
            }
            return Arrays.equals(signatureBytes, (streamSignature + htsVersion).getBytes());
        } catch (IOException e) {
            throw new HtsjdkIOException(String.format("Failure reading content from stream for %s", sourceName), e);
        }
    }

    @Override
    public HtsTestDecoder getDecoder(final IOPath inputPath) {
        return getDecoder(inputPath, new HtsTestDecoderOptions());
    }

    @Override
    public HtsTestDecoder getDecoder(final IOPath inputPath, final HtsTestDecoderOptions decoderOptions) {
        return new HtsTestDecoder(inputPath, decoderOptions, htsFormat, htsVersion);
    }

    @Override
    public HtsTestDecoder getDecoder(final Bundle inputBundle, final HtsTestDecoderOptions decoderOptions) {
        return new HtsTestDecoder(inputBundle, decoderOptions, htsFormat, htsVersion);
    }

    @Override
    public HtsTestDecoder getDecoder(final InputStream is, final String displayName) {
        return getDecoder(is, displayName, new HtsTestDecoderOptions());
    }

    @Override
    public HtsTestDecoder getDecoder(final InputStream is, final String displayName, final HtsTestDecoderOptions decoderOptions) {
        return new HtsTestDecoder(is, displayName, decoderOptions, htsFormat, htsVersion);
    }

    @Override
    public HtsTestEncoder getEncoder(final IOPath outputPath) {
        return new HtsTestEncoder(outputPath, htsFormat, htsVersion);
    }

    @Override
    public HtsTestEncoder getEncoder(final IOPath outputPath, final HtsTestEncoderOptions encoderOptions) {
        return new HtsTestEncoder(outputPath, encoderOptions, htsFormat, htsVersion);
    }

    @Override
    public HtsTestEncoder getEncoder(final OutputStream os, final String displayName) {
        return new HtsTestEncoder(os, displayName, htsFormat, htsVersion);
    }

    @Override
    public HtsTestEncoder getEncoder(final OutputStream os, final String displayName, final HtsTestEncoderOptions encoderOptions) {
        return new HtsTestEncoder(os, displayName, encoderOptions, htsFormat, htsVersion);
    }

    @Override
    public boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion) {
        throw new RuntimeException("Not yet implemented");
    }

}
