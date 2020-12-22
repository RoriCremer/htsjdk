package htsjdk.plugin;

/**
 * A codec category defines a set of codecs that expose the same interfaces (header, record, etc).
 * Within a category, each codec supports a specific file format/version/protocol.
 */
public enum HtsCodecCategory {

    // TODO: should this be called SEQUENCE, since ultimately FASTQ should belong in the same category..
    HAPLOID_REFERENCE,      // FASTA
    ALIGNED_READS,          // SAM, BAM, CRAM, htsget, sra,...
    VARIANTS,               // VCF, BCF
    FEATURES,               // GFF, BED, etc
}
