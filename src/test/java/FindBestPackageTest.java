import com.google.common.io.Resources;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

import java.net.URL;

// Checks that no exceptions occur when executing the main program
public class FindBestPackageTest {
    private String getPath(URL url){
        String path = url.getPath();
        if(SystemUtils.IS_OS_WINDOWS){
            path = path.substring(1); //remove the leading '/'
        }
        return path;
    }

    @Test
    public void testOk() {
        String path = getPath(Resources.getResource("sampleInput.txt"));
        String[] args = {path};
        FindBestPackage.main(args);
    }

    @Test
    public void testWithErrors() {
        String path = getPath(Resources.getResource("sampleInvalidInput.txt"));
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
