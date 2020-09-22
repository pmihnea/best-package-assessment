import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import java.util.BitSet;
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
    private static final Pattern PRODUCT_DELIMITER_PATTERN = Pattern.compile("[(),€]+");
    //strict product pattern - not use because it doesn't allow precised error messages
    //public static final Pattern PRODUCT_PATTERN = Pattern.compile("\\(\\d+,\\d+(\\.\\d+)?,€\\d+(\\.\\d+)?\\)");
    //relaxed product pattern to allow more precise error messages of the components
    public static final Pattern PRODUCT_PATTERN = Pattern.compile("\\([^,]+,[^,]+,€[^\\)]+\\)");

    // token names
    static final String MAX_WEIGHT = "max weight";
    static final String MAX_PRODUCTS = "max products";
    static final String PRODUCT_NUMBER = "product number";
    static final String PRODUCT_WEIGHT = "product weight";
    static final String PRODUCT_PRICE = "product price";
    static final String PRODUCT_STRUCTURE = "product structure";
    static final String COLON_DELIMITER = ": delimiter";

    // parsing fields
    private int lineNumber = 0;

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
        try (Scanner scanner = new Scanner(stringLine)) {
            this.maxWeight = getTokenValueOrElseThrow(scanner::nextDouble, MAX_WEIGHT, scanner);
            skipDelimiterOrElseThrow(":", COLON_DELIMITER, scanner);
            this.products = Sets.newHashSet();
            // Raw product sample "81 : (1,53.38,€45) (2,88.62,€98)"
            while (scanner.hasNext()) {
                String rawProduct = getTokenValueOrElseThrow(() -> scanner.next(PRODUCT_PATTERN), PRODUCT_STRUCTURE, scanner);
                try(Scanner productScanner = new Scanner(rawProduct).useDelimiter(PRODUCT_DELIMITER_PATTERN)) {
                    int productNumber = getTokenValueOrElseThrow(productScanner::nextInt, PRODUCT_NUMBER, productScanner);
                    double productWeight = getTokenValueOrElseThrow(productScanner::nextDouble, PRODUCT_WEIGHT, productScanner);
                    double productPrice = getTokenValueOrElseThrow(productScanner::nextDouble, PRODUCT_PRICE, productScanner);
                    products.add(new Product(productNumber, productWeight, productPrice));
                }
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

    private void skipDelimiterOrElseThrow(String delimiter, String delimiterName, Scanner scanner) {
        getTokenValueOrElseThrow(() -> scanner.next(delimiter), delimiterName, scanner);
    }

    private <T> T getTokenValueOrElseThrow(Supplier<T> supplier, String tokenName, Scanner scanner) {
        try{
            return supplier.get();
        }catch(Exception e) {
            throw new PackageSpecificationParsingException(
                lineNumber,
                tokenName,
                scanner.hasNextLine() ? scanner.nextLine() : "<EOL>",
                e);
        }
    }

    Double getMaxWeight() {
        return maxWeight;
    }

    Set<Product> getProducts() {
        return products;
    }

    public enum FindBestPackageStrategy {
        OPTIMIZED, BRUTE_FORCE, KNAPSACK
    }

    private FindBestPackageStrategy findBestPackageStrategy = FindBestPackageStrategy.OPTIMIZED;

    public FindBestPackageStrategy getFindBestPackageStrategy() {
        return findBestPackageStrategy;
    }

    public void setFindBestPackageStrategy(FindBestPackageStrategy findBestPackageStrategy) {
        this.findBestPackageStrategy = findBestPackageStrategy;
    }

    /**
     * Finds the best package based on the valid package specification.
     *
     * @return Optional.of(best package) or Optional.empty in case none is found
     */
    public Optional<Package> findBestPackage() {
        switch (getFindBestPackageStrategy()) {
            case OPTIMIZED:
                return findBestPackageOptimized();
            case BRUTE_FORCE:
                return findBestPackageBruteForce();
            case KNAPSACK:
                return findBestPackageKnapsack();
            default:
                throw new IllegalStateException("Invalid FindBestPackageStrategy: " + getFindBestPackageStrategy());
        }
    }

    /**
     * Finds the best package by iterating through all possible packages and selecting the best one.
     */
    private Optional<Package> findBestPackageBruteForce() {
        // generate all subsets of the given set of products
        // as the max number of products is <= 15 the Guava Sets.powerSet algorithm can be used
        Set<Set<Product>> allPackages = Sets.powerSet(products);
        // find the best subset
        return allPackages.stream()
            .map(Package::new)
            .filter(aPackage -> aPackage.getWeight() <= maxWeight)
            .max(Package.BEST_PACKAGE_COMPARATOR);
    }

    /**
     * Finds the best package by iterating through all possible packages and selecting the best one,
     * but the iteration is optimized and does not iterate through the packages that already exceed the max weight
     * and through those that are an extension of the former.
     */
    private Optional<Package> findBestPackageOptimized() {
        Product[] productsArray = products.toArray(new Product[0]);
        // Produces a stream of combinations of indexes of products
        // with a condition that stops adding another product into a combination of products
        // if that would exceed the max package weight
        CombinationsStream combinationsStream = new CombinationsStream(productsArray.length,
            (bitSet, i) -> {
                Double productsTotalWeight = bitSet.stream()
                    .mapToObj(index -> productsArray[index])
                    .map(Product::getWeight)
                    .reduce(Double::sum)
                    .orElse(0.0);
                return productsTotalWeight + productsArray[i].getWeight() <= maxWeight;
            }
        );
        // The stream of combinations of indexes of products is transformed to a stream of Packages
        // and then the best package is extracted
        return combinationsStream.toBitSetStream()
            .map(bitSet -> bitSet.stream()
                .mapToObj(index -> productsArray[index])
                .collect(Collectors.toSet()))
            .map(Package::new)
            .max(Package.BEST_PACKAGE_COMPARATOR);
    }

    /**
     * Finds the best package using the classical Knapsack algorithm.
     */
    private Optional<Package> findBestPackageKnapsack() {
        Product[] productsArray = products.toArray(new Product[0]);
        BitSet max = new Knapsack(productsArray).findMax(getMaxWeight());
        Set<Product> packageProducts = max.stream().mapToObj(index -> productsArray[index]).collect(Collectors.toSet());
        return Optional.of(new Package(packageProducts));
    }
}
