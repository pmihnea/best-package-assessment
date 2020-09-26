import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The class reads an input text file where each row contains a package specification and
 * computes for each row the best package in terms of maximum total price and minimum total weight in case of the same total price.
 * See the README.md for the complete requirements.<br>
 * The main processing steps are: <ul>
 * <li>it reads the file line by line
 * <li>for each line <ul>
 * <li>it parses and validates the package specification resulting a set of products with a max total weight
 * <li>it finds the best package
 * <li>it prints the resulting package on one line
 * </ul>
 * </ul>
 */
public class FindBestPackageCommand {
    /**
     * Processes the input file line by line.
     *
     * @param args only one argument: the input file path
     */
    public static void main(String[] args) {
        if (args != null && args.length == 1) {
            try (Stream<String> linesStream = Files.lines(Path.of(args[0]), StandardCharsets.UTF_8)) {
                io.vavr.collection.Stream.ofAll(linesStream)
                    .zipWithIndex()
                    .filter(lineWithNumber -> StringUtils.isNotBlank(lineWithNumber._1()))
                    .forEach(lineWithNumber -> processLine(lineWithNumber._1(), lineWithNumber._2()));
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        } else {
            System.err.println("Invalid command arguments. One argument is required: the input file path.");
        }
    }

    /**
     * Processes one text line containing the package specifications and prints the best package.
     *
     * @param line       the package specifications
     * @param lineNumber the number of the line in the original file
     */
    private static void processLine(String line, int lineNumber) {
        try {
            Optional<Package> optionalBestPackage =
                new PackageSpecificationReader()
                .andThen(new PackageSpecificationValidator())
                .andThen(new BestPackageFinder().withFindBestPackageStrategy(BestPackageFinder.FindBestPackageStrategy.KNAPSACK))
                .apply(new InputLine(lineNumber, line));

            System.out.println(new OutputLine(optionalBestPackage));
        } catch (PackageSpecificationBaseException e) {
            System.out.println("-"); //no package could be found because of errors
            System.err.println("Line " + lineNumber + " cannot be processed because :" + System.lineSeparator() + e.getMessage());
        }
    }
}
