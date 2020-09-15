import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;

// Checks that no exceptions occur when executing the main program
public class FindBestPackageTest {
    @Test
    public void testOk() {
        String path = Resources.getResource("sampleInput.txt").getPath().substring(1); //remove the leading '/'
        String[] args = {path};
        FindBestPackage.main(args);
    }

    @Test
    public void testWithErrors() {
        String path = Resources.getResource("sampleInvalidInput.txt").getPath().substring(1); //remove the leading '/'
        String[] args = {path};
        FindBestPackage.main(args);
    }

    @Test
    public void testWithNoFile() {
        String[] args = {};
        FindBestPackage.main(args);
    }

    @Test
    public void testWithNotExistingFile() {
        String[] args = {"not-existing-file.txt"};
        FindBestPackage.main(args);
    }
}
