package htsjdk.plugin;

public interface Upgradeable {
    boolean runVersionUpgrade(final HtsCodecVersion sourceCodecVersion, final HtsCodecVersion targetCodecVersion);
}
