package htsjdk.plugin.reads;

import htsjdk.io.IOPath;
import htsjdk.plugin.bundle.BundleResource;
import htsjdk.utils.ValidationUtils;

public class ReadsResource implements BundleResource<ReadsResourceType> {
    final IOPath resourcePath;
    final ReadsResourceType resourceType;

    public ReadsResource(final IOPath resourcePath, final ReadsResourceType resourceType) {
        ValidationUtils.validateArg(resourcePath != null, "resource path must be non-null");
        ValidationUtils.validateArg(resourceType != null, "resource type must be non-null");
        this.resourcePath = resourcePath;
        this.resourceType = resourceType;
    }

    @Override
    public IOPath getResourcePath() {
        return resourcePath;
    }

    @Override
    public ReadsResourceType getResourceType() {
        return resourceType;
    }
}
