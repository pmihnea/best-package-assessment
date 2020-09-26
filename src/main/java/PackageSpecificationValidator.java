import com.google.common.collect.ImmutableList;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Encapsulates the function that validates a package specification based on the given constraints.
 */
public class PackageSpecificationValidator implements Function<RawPackageSpecification, PackageSpecification> {

    /**
     * Validates the parsed package specification based on the given constraints.
     *
     * @throws PackageSpecificationValidationException in case one or more constraints are not met
     */
    public PackageSpecification validateTokens(RawPackageSpecification rawPackageSpecification) throws PackageSpecificationValidationException {
        int lineNumber = rawPackageSpecification.getLineNumber();
        double maxWeight = rawPackageSpecification.getMaxWeight();
        Set<Product> products = rawPackageSpecification.getProducts();

        // validate the global constraints
        Stream<String> globalErrors = validateGlobalConstraints(lineNumber, maxWeight, products.size());

        // validate all products constraints
        Stream<String> productsErrors = products.stream()
            .flatMap(product -> validateProductConstraints(product, lineNumber));

        // create a single error message
        String errors = Stream.concat(globalErrors, productsErrors).collect(Collectors.joining(System.lineSeparator()));
        if (!errors.isEmpty()) {
            throw new PackageSpecificationValidationException(errors, lineNumber);
        }

        return new PackageSpecification(lineNumber, maxWeight, products);
    }

    private Stream<String> validateGlobalConstraints(int lineNumber, double maxWeight, int productsSize) {
        return ImmutableList.of(
            validateToken(maxWeight <= 100.0, InputLineTokens.MAX_WEIGHT, maxWeight, lineNumber),
            validateToken(productsSize <= 15, InputLineTokens.MAX_PRODUCTS, productsSize, lineNumber)
        ).stream().filter(Optional::isPresent).map(Optional::get);
    }

    private Stream<String> validateProductConstraints(Product product, int lineNumber) {
        return ImmutableList.of(
            validateToken(product.getNumber() <= 15, InputLineTokens.PRODUCT_NUMBER, product.getNumber(), lineNumber),
            validateToken(product.getWeight() <= 100.0, InputLineTokens.PRODUCT_WEIGHT, product.getWeight(), lineNumber),
            validateToken(product.getPrice() <= 100.0, InputLineTokens.PRODUCT_PRICE, product.getPrice(), lineNumber)
        ).stream().filter(Optional::isPresent).map(Optional::get);
    }

    private Optional<String> validateToken(boolean expression, String tokenName, Object tokenValue, int lineNumber) {
        if (!expression) {
            return Optional.of("On line " + lineNumber + " the '" + tokenName + "' has an invalid value = '" + tokenValue + "'.");
        } else {
            return Optional.empty();
        }
    }

    @Override
    public PackageSpecification apply(RawPackageSpecification rawPackageSpecification) {
        return validateTokens(rawPackageSpecification);
    }
}
