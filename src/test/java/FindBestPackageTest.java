import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
        String path = getPath(getClass().getResource("sampleInput.txt"));
        String[] args = {path};
        FindBestPackage.main(args);
    }

    @Test
    public void testWithErrors() {
        String path = getPath(getClass().getResource("sampleInvalidInput.txt"));
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

    @Test
    public void testWithBigFile() {
        PrintStream current = System.out;
        long startTS = System.currentTimeMillis();
        try(PrintStream nullPrintStream = createNullPrintStream()) {
            System.setOut(nullPrintStream);
            String path = getPath(getClass().getResource("sampleBigInput.txt"));
            String[] args = {path};
            FindBestPackage.main(args);
        }finally {
            System.setOut(current);
        }
        long endTS = System.currentTimeMillis();
        System.out.println("testWithBigFile finished in " + (endTS-startTS) + " ms.");
    }

    private static PrintStream createNullPrintStream() {
        return new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                //
            }
        });
    }
}
