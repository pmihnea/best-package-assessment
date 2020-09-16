import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import io.vavr.control.Try;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Encapsulates an input line containing a package specification composed of:<ul>
 * <li>max weight
 * <li>more products composed of: product number, weight, price
 * </ul>
 * and the function that finds the best package.
 */
public class PackageSpecification {
    /**
     * Delimiter patter for such expression:<br>
     * <code>81 : (1,53.38,€45) (2,88.62,€98)</code>
     */
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("[ :(),€]+");

    // token names
    private static final String MAX_WEIGHT = "max weight";
    private static final String MAX_PRODUCTS = "max products";
    private static final String PRODUCT_NUMBER = "product number";
    private static final String PRODUCT_WEIGHT = "product weight";
    private static final String PRODUCT_PRICE = "product price";

    // parsing fields
    private int lineNumber = 0;
    private Scanner scanner;

    // package specification fields
    private Double maxWeight;
    private Set<Product> products;

    // constructor used for testing
    PackageSpecification(Double maxWeight, Set<Product> products) throws PackageSpecificationValidationException {
        this.maxWeight = maxWeight;
        this.products = products;
        validateTokens();
    }

    // constructor used for testing
    PackageSpecification(Double maxWeight, Product... products) throws PackageSpecificationValidationException {
        this(maxWeight, Sets.newHashSet(products));
    }

    /**
     * Constructs a valid package specification
     *
     * @param stringLine input text line
     * @param lineNumber input line number
     * @throws PackageSpecificationBaseException in case a valid package specification cannot be constructed
     */
    public PackageSpecification(String stringLine, int lineNumber) throws PackageSpecificationBaseException {
        this.lineNumber = lineNumber;
        readTokens(stringLine);
        validateTokens();
    }

    /**
     * Parses a text line into tokens that represent a package specification
     *
     * @param stringLine the text line
     * @throws PackageSpecificationParsingException in case the tokens are not parsed correctly
     */
    private void readTokens(String stringLine) throws PackageSpecificationParsingException {
        // use a scanner to split the line in valuable tokens ignoring the not needed delimiters
        // this approach actually allows a more relax format of the input line, by using only one type of delimiter
        try (Scanner scanner = new Scanner(stringLine).useDelimiter(DELIMITER_PATTERN)) {
            this.scanner = scanner;
            this.maxWeight = getTokenValueOrElseThrow(scanner::nextDouble, MAX_WEIGHT);
            this.products = Sets.newHashSet();
            while (scanner.hasNext()) {
                int productNumber = getTokenValueOrElseThrow(scanner::nextInt, PRODUCT_NUMBER);
                double productWeight = getTokenValueOrElseThrow(scanner::nextDouble, PRODUCT_WEIGHT);
                double productPrice = getTokenValueOrElseThrow(scanner::nextDouble, PRODUCT_PRICE);
                products.add(new Product(productNumber, productWeight, productPrice));
            }
        }
    }

    /**
     * Validates the parsed package specification based on the given constraints.
     *
     * @throws PackageSpecificationValidationException in case one or more constraints are not met
     */
    private void validateTokens() throws PackageSpecificationValidationException {
        // validate the global constraints
        Stream<String> globalErrors = validateGlobalConstraints();

        // validate all products constraints
        Stream<String> productsErrors = getProducts().stream()
            .flatMap(this::validateProductConstraints);

        // create a single error message
        String errors = Stream.concat(globalErrors, productsErrors).collect(Collectors.joining(System.lineSeparator()));
        if (!errors.isEmpty()) {
            throw new PackageSpecificationValidationException(errors, lineNumber);
        }
    }

    private Stream<String> validateGlobalConstraints() {
        return ImmutableList.of(
            validateToken(getMaxWeight() <= 100.0, MAX_WEIGHT, getMaxWeight()),
            validateToken(getProducts().size() <= 15, MAX_PRODUCTS, getProducts().size())
        ).stream().filter(Optional::isPresent).map(Optional::get);
    }

    private Stream<String> validateProductConstraints(Product product) {
        return ImmutableList.of(
            validateToken(product.getNumber() <= 15, PRODUCT_NUMBER, product.getNumber()),
            validateToken(product.getWeight() <= 100.0, PRODUCT_WEIGHT, product.getWeight()),
            validateToken(product.getPrice() <= 100.0, PRODUCT_PRICE, product.getPrice())
        ).stream().filter(Optional::isPresent).map(Optional::get);
    }

    private Optional<String> validateToken(boolean expression, String tokenName, Object tokenValue) {
        if (!expression) {
            return Optional.of("On line " + this.lineNumber + " the '" + tokenName + "' has an invalid value = '" + tokenValue + "'.");
        } else {
            return Optional.empty();
        }
    }

    private <T> T getTokenValueOrElseThrow(Supplier<T> supplier, String tokenName) {
        return Try.ofSupplier(supplier).getOrElseThrow(
            () -> new PackageSpecificationParsingException(
                lineNumber,
                tokenName,
                scanner.hasNext() ? scanner.next() : "<EOL>"));
    }

    Double getMaxWeight() {
        return maxWeight;
    }

    Set<Product> getProducts() {
        return products;
    }

    /**
     * Finds the best package based on the valid package specification.
     *
     * @return Optional.of(best package) or Optional.empty in case none is found
     */
    public Optional<Package> findBestPackage() {
        // generate all subsets of the given set of products
        // as the max number of products is <= 15 the Guava Sets.powerSet algorithm can be used
        Set<Set<Product>> allPackages = Sets.powerSet(products);
        // find the best subset
        return allPackages.stream()
            .map(Package::new)
            .filter(aPackage -> aPackage.hasValidWeight(maxWeight))
            .max(Package.BEST_PACKAGE_COMPARATOR);
    }
}
