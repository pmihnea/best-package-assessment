package com.pmihnea.bestpackage.packagefinder;

import com.google.common.collect.Sets;
import com.pmihnea.bestpackage.Package;
import com.pmihnea.bestpackage.PackageSpecification;
import com.pmihnea.bestpackage.Product;

import java.util.Optional;
import java.util.Set;

/**
 * Finds the best package by iterating through all possible packages and selecting the best one.
 */
public class BruteForceBestPackageFinder implements PackageFinder {
    private Optional<Package> findBestPackageBruteForce(PackageSpecification packageSpecification) {
        // generate all subsets of the given set of products
        // as the max number of products is <= 15 the Guava Sets.powerSet algorithm can be used
        Set<Set<Product>> allPackages = Sets.powerSet(packageSpecification.getProducts());
        // find the best subset
        return allPackages.stream()
            .map(Package::new)
            .filter(aPackage -> aPackage.getWeight() <= packageSpecification.getMaxWeight())
            .max(Package.BEST_PACKAGE_COMPARATOR);
    }

    @Override
    public Optional<Package> apply(PackageSpecification packageSpecification) {
        return findBestPackageBruteForce(packageSpecification);
    }
}
