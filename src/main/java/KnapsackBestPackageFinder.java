import java.util.BitSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Finds the best package using the classical Knapsack algorithm.
 */
public class KnapsackBestPackageFinder implements PackageFinder {

    private Optional<Package> findBestPackageKnapsack(PackageSpecification packageSpecification) {
        Product[] productsArray = packageSpecification.getProducts().toArray(new Product[0]);
        BitSet max = new Knapsack(productsArray).findMax(packageSpecification.getMaxWeight());
        Set<Product> packageProducts = max.stream().mapToObj(index -> productsArray[index]).collect(Collectors.toSet());
        return Optional.of(new Package(packageProducts));
    }

    @Override
    public Optional<Package> apply(PackageSpecification packageSpecification) {
        return findBestPackageKnapsack(packageSpecification);
    }
}
