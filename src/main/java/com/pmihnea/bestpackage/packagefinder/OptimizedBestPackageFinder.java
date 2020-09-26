package com.pmihnea.bestpackage.packagefinder;

import com.pmihnea.bestpackage.Package;
import com.pmihnea.bestpackage.PackageSpecification;
import com.pmihnea.bestpackage.Product;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Finds the best package by iterating through all possible packages and selecting the best one,
 * but the iteration is optimized and does not iterate through the packages that already exceed the max weight
 * and through those that are an extension of the former.
 */
public class OptimizedBestPackageFinder implements PackageFinder {

    private Optional<Package> findBestPackageOptimized(PackageSpecification packageSpecification) {
        Product[] productsArray = packageSpecification.getProducts().toArray(new Product[0]);
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
                return productsTotalWeight + productsArray[i].getWeight() <= packageSpecification.getMaxWeight();
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

    @Override
    public Optional<Package> apply(PackageSpecification packageSpecification) {
        return findBestPackageOptimized(packageSpecification);
    }
}
