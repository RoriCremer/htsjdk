package htsjdk.plugin;

public class UnusedType {

    public static UnusedType unusedType = new UnusedType();

    private UnusedType() {}

    final UnusedType get() { return unusedType; }
}
