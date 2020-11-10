package htsjdk.plugin.reads;

import htsjdk.io.IOPath;
import htsjdk.plugin.bundle.Bundle;
import htsjdk.utils.ValidationUtils;

import java.util.Optional;

public class ReadsBundle extends Bundle<ReadsResourceType> {

    //TODO: add md5 overloads
    public ReadsBundle(final IOPath readsPath) {
        add(new ReadsResource(readsPath, ReadsResourceType.READS));
    }

    public ReadsBundle(final IOPath readsPath, final IOPath indexPath) {
        ValidationUtils.nonNull(readsPath, "reads path must not be null");
        ValidationUtils.nonNull(indexPath, "reads index must not be null");
        add(new ReadsResource(readsPath, ReadsResourceType.READS));
        add(new ReadsResource(indexPath, ReadsResourceType.INDEX));
    }

    public IOPath getReads() {
        return get(ReadsResourceType.READS).get();
    }

    public Optional<IOPath> getIndex() {
        return get(ReadsResourceType.INDEX);
    }

}
