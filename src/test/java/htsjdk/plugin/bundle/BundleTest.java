package htsjdk.plugin.bundle;

import htsjdk.HtsjdkTest;
import htsjdk.io.HtsPath;
import htsjdk.io.IOPath;
import org.junit.Test;

public class BundleTest extends HtsjdkTest {

//TODO: add JSON schema validation to these tests
//        // A simple schema that accepts only JSON objects with a mandatory property 'id'.
//        Json.Schema schema = Json.schema(Json.object("type", "object", "required", Json.array("id")));
//        schema.validate(Json.object("id", 666, "name", "Britlan")); // true
//        schema.validate(Json.object("ID", 666, "name", "Britlan")); // false

    @Test
    public void testJSON() {
        final IOPath readsPath = new HtsPath("myFile.bam");
        final IOPath readsIndex = new HtsPath("myFile.bai");
        final InputBundle inputBundle = InputBundleBuilder.start()
                .add(new InputIOPathResource(BundleResourceType.READS, readsPath))
                .add(new InputIOPathResource(BundleResourceType.INDEX, readsIndex)).getBundle();
        final String jsonString = inputBundle.serializeToJSON();
        System.out.println(jsonString);

    }
}
