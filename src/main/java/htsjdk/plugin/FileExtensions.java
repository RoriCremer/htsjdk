package htsjdk.plugin;

import htsjdk.io.IOPath;
import htsjdk.plugin.reads.ReadsFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//TODO: Move this to CodecByType
public class FileExtensions {

    final static Map<String, ReadsFormat> extensionMap = new HashMap<String, ReadsFormat>() {{
        this.put(".bam", ReadsFormat.BAM);
    }};

    public static Optional<ReadsFormat> getReadsFormat(final IOPath outputPath) {
        final Optional<String> outputExtension = outputPath.getExtension();
        if (!outputExtension.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(extensionMap.get(outputExtension.get()));
    }


}
