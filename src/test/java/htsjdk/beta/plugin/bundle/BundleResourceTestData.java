package htsjdk.beta.plugin.bundle;

import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BundleResourceTestData {
    public final static IOPath READS_FILE = new HtsPath("myreads.bam");
    public final static IOPath READS_INDEX = new HtsPath("myreads.bai");

    public static final BundleResource readsWithSubContentType = new IOPathResource(
            READS_FILE,
            BundleResourceType.READS,
            BundleResourceType.READS_BAM);
    public static final BundleResource readsNoSubContentType =new IOPathResource(
            READS_FILE,
            BundleResourceType.READS);
    public static final BundleResource indexWithSubContentType = new IOPathResource(
            READS_INDEX,
            BundleResourceType.READS_INDEX,
            BundleResourceType.READS_INDEX_BAI);
    public static final BundleResource indexNoSubContentType = new IOPathResource(
            READS_INDEX,
            BundleResourceType.READS_INDEX);

    public final static class CustomHtsPath extends HtsPath {
        public CustomHtsPath(final String pathString) {
            super(pathString);
        }
    }

    // streams for making bundle resources that never need closing
    public static final InputStream fakeInputStream = new InputStream() {
        @Override
        public int read() throws IOException {
            throw new IllegalStateException();
        }
    };
    public static final OutputStream fakeOutputStream = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            throw new IllegalStateException();
        }
    };

}
