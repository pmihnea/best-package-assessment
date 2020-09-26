import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

// Checks that no exceptions occur when executing the main program
public class FindBestPackageTest {
    private String getPath(URL url) {
        String path = url.getPath();
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.substring(1); //remove the leading '/'
        }
        return path;
    }

    private PrintStream err, out;
    private ByteArrayOutputStream testErr, testOut;

    @BeforeEach
    public void setupTestInOutStreams() {
        out = System.out;
        err = System.err;
        testOut = new ByteArrayOutputStream();
        testErr = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        System.setErr(new PrintStream(testErr));
    }

    @AfterEach
    public void setupOriginalInOutStreams() throws IOException {
        testOut.close();
        testErr.close();
        System.setOut(out);
        System.setErr(err);
    }

    @Test
    public void testOk() {
        String path = getPath(getClass().getResource("sampleInput.txt"));
        String[] args = {path};
        FindBestPackage.main(args);
        System.out.flush();
        Assertions.assertIterableEquals(List.of(
            "4",
            "-",
            "2,7",
            "8,9"
            ),
            testOutToList()
        );
    }

    private List<String> testOutToList() {
        System.out.flush();
        return byteStreamToStringList(testOut);
    }

    private List<String> testErrToList() {
        System.err.flush();
        return byteStreamToStringList(testErr);
    }

    private List<String> byteStreamToStringList(ByteArrayOutputStream byteStream) {
        return new BufferedReader(
            new InputStreamReader(
                new ByteArrayInputStream(byteStream.toByteArray())))
            .lines()
            .collect(Collectors.toList());
    }


    @Test
    public void testWithErrors() {
        String path = getPath(getClass().getResource("sampleInvalidInput.txt"));
        String[] args = {path};
        FindBestPackage.main(args);
        Assertions.assertLinesMatch(List.of(">> other lines >>", "Invalid .*",
            ">> 7 >>"),
            testErrToList());

    }

    @Test
    public void testWithNoFile() {
        String[] args = {};
        FindBestPackage.main(args);
        Assertions.assertLinesMatch(List.of("Invalid command arguments.*"),
            testErrToList());

    }

    @Test
    public void testWithNotExistingFile() {
        String[] args = {"not-existing-file.txt"};
        FindBestPackage.main(args);
        Assertions.assertLinesMatch(List.of("java.nio.file.NoSuchFileException.*"),
            testErrToList());
    }

    @Test
    public void testWithBigFile() {
        PrintStream current = System.out;
        long startTS = System.currentTimeMillis();
        try (PrintStream nullPrintStream = createNullPrintStream()) {
            System.setOut(nullPrintStream);
            String path = getPath(getClass().getResource("sampleBigInput.txt"));
            String[] args = {path};
            FindBestPackage.main(args);
        } finally {
            System.setOut(current);
        }
        long endTS = System.currentTimeMillis();
        System.out.println("testWithBigFile finished in " + (endTS - startTS) + " ms.");
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
