package htsjdk.plugin;

import java.io.Closeable;

/**
 * Base interface for encoders.
 *
 * @param <FORMAT> enum type param containing constants for each format supported by this codec
 *               (i.e., ReadsFormat defining SAM/BAM/CRAM constants)
 * @param <HEADER> type param for the header for this format (i.e. SAMFileHeader)
 * @param <RECORD> type param for the record for this format (i.e. SAMRecord)
 */
public interface HtsEncoder<FORMAT, HEADER extends HtsHeader, RECORD extends HtsRecord> extends Closeable {

    FORMAT getFormat();

    HtsCodecVersion getVersion();

    String getDisplayName();

    void setHeader(HEADER header);

    void write(RECORD record);

    void close();

}
