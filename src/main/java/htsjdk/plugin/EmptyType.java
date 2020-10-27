package htsjdk.plugin;

public class EmptyType {

    public static EmptyType emptyType = new EmptyType();

    private EmptyType() {}

    final EmptyType get() { return emptyType; }
}
