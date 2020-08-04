package htsjdk.plugin;

public enum HtsCodecType {

    REFERENCE,
        // fasta

    ALIGNED_READS,
        //    SAM <SamReader, SAMFileWriter>
        //    BAM,<SamReader, SAMFileWriter>
        //    CRAM,<SamReader, SAMFileWriter>

    VARIANTS,         // VariantContext
        //    VCF,
        //    BCF

    FEATURES,         // ??
}
