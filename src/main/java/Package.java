import com.google.common.base.MoreObjects;

import java.util.Comparator;
import java.util.Set;

/**
 * Encapsulates a package with a set of products and their total weight and price functions.
 */
public class Package {
    private final Set<Product> products;

    // Best package comparator
    public static final Comparator<Package> BEST_PACKAGE_COMPARATOR =
        // compare first by price
        Comparator.comparingDouble(Package::getPrice)
            .thenComparing(
                // and in case of equality compare by weight in reverse order
                Comparator.comparingDouble(Package::getWeight).reversed()
            );

    public Set<Product> getProducts() {
        return products;
    }

    public Package(Set<Product> products) {
        this.products = products;
    }

    public Double getPrice() {
        return products.stream()
            .map(Product::getPrice)
            .reduce(Double::sum)
            .orElse(0.0);
    }

    public Double getWeight() {
        return products.stream()
            .map(Product::getWeight)
            .reduce(Double::sum)
            .orElse(0.0);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("products", products)
            .toString();
    }
}
