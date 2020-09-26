import com.google.common.collect.Sets;
import io.vavr.control.Try;

import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Encapsulates the function that reads a package specification from a input text line.
 */
public class PackageSpecificationReader implements Function<InputLine, RawPackageSpecification> {
    /**
     * Line structure and delimiter patters for such expression:<br>
     * <code>81 : (1,53.38,€45) (2,88.62,€98)</code>
     */
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("[ :(),€]+");
    // private static final Pattern LINE_STRUCTURE_PATTERN = Pattern.compile("^(\\d+(\\.\\d+)?) +:( +\\(\\d+,\\d+(\\.\\d+)?,€\\d+(\\.\\d+)?\\))+$");
    // Use a relaxed pattern that only checks the structure of the line given by the delimiters but not the values themselves
    // that are checked later to be able to give a precise error on them.
    // The regular expression check only gives a yes/no match response.
    private static final Pattern LINE_STRUCTURE_PATTERN_RELAXED =
        Pattern.compile("^([^ :(),€]+) +:( +\\(([^ :(),€]+),([^ :(),€]+),€([^ :(),€]+)\\))+$");

    /**
     * Parses a text line into tokens that represent a package specification
     *
     * @throws PackageSpecificationParsingException in case the tokens are not parsed correctly
     */
    public RawPackageSpecification readTokens(InputLine inputLine) throws PackageSpecificationParsingException {
        int lineNumber = inputLine.getLineNumber();
        String stringLine = inputLine.getStringLine();

        // package specification fields
        Double maxWeight;
        Set<Product> products;

        // check first the line structure
        if (!LINE_STRUCTURE_PATTERN_RELAXED.matcher(stringLine).matches()) {
            throw new PackageSpecificationParsingException(
                lineNumber,
                InputLineTokens.LINE_STRUCTURE,
                stringLine);
        }
        // use a scanner to split the line in valuable tokens ignoring the delimiters that were check upfront
        try (Scanner scanner = new Scanner(stringLine).useDelimiter(DELIMITER_PATTERN)) {
            maxWeight = getTokenValueOrElseThrow(scanner::nextDouble, InputLineTokens.MAX_WEIGHT, scanner, lineNumber);
            products = Sets.newHashSet();
            while (scanner.hasNext()) {
                int productNumber = getTokenValueOrElseThrow(scanner::nextInt, InputLineTokens.PRODUCT_NUMBER, scanner, lineNumber);
                double productWeight = getTokenValueOrElseThrow(scanner::nextDouble, InputLineTokens.PRODUCT_WEIGHT, scanner, lineNumber);
                double productPrice = getTokenValueOrElseThrow(scanner::nextDouble, InputLineTokens.PRODUCT_PRICE, scanner, lineNumber);
                products.add(new Product(productNumber, productWeight, productPrice));
            }
        }

        return new RawPackageSpecification(lineNumber, maxWeight, products);
    }

    private <T> T getTokenValueOrElseThrow(Supplier<T> supplier, String tokenName, Scanner scanner, int lineNumber) {
        return Try.ofSupplier(supplier).getOrElseThrow(
            () -> new PackageSpecificationParsingException(
                lineNumber,
                tokenName,
                scanner.hasNext() ? scanner.next() : "<EOL>"));
    }

    @Override
    public RawPackageSpecification apply(InputLine inputLine) {
        return readTokens(inputLine);
    }
}
