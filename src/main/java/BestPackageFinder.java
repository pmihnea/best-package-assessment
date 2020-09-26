import com.google.common.base.Preconditions;

import java.util.Optional;

/**
 * Facade class to use several strategies to find the best package based on a valid package specification.
 */
public class BestPackageFinder implements PackageFinder {
    @Override
    public Optional<Package> apply(PackageSpecification packageSpecification) {
        return findBestPackage(packageSpecification);
    }

    public enum FindBestPackageStrategy {
        OPTIMIZED, BRUTE_FORCE, KNAPSACK
    }

    private FindBestPackageStrategy findBestPackageStrategy = FindBestPackageStrategy.OPTIMIZED;
    private PackageFinder externalPackageFinder;

    public FindBestPackageStrategy getFindBestPackageStrategy() {
        return findBestPackageStrategy;
    }

    public BestPackageFinder withFindBestPackageStrategy(FindBestPackageStrategy findBestPackageStrategy) {
        Preconditions.checkNotNull(findBestPackageStrategy);
        this.findBestPackageStrategy = findBestPackageStrategy;
        return this;
    }

    public BestPackageFinder withPackageFinder(PackageFinder packageFinder) {
        Preconditions.checkNotNull(packageFinder);
        this.findBestPackageStrategy = null;
        this.externalPackageFinder = packageFinder;
        return this;
    }

    /**
     * Finds the best package based on the valid package specification.
     *
     * @return Optional.of(best package) or Optional.empty in case none is found
     * @param packageSpecification package specification
     */
    public Optional<Package> findBestPackage(PackageSpecification packageSpecification) {
        PackageFinder packageFinder = externalPackageFinder;
        if(packageFinder == null) {
            // TODO: change the code below to switch expressions in JDK.14
            switch (getFindBestPackageStrategy()) {
                case OPTIMIZED:
                    packageFinder = new OptimizedBestPackageFinder();
                    break;
                case BRUTE_FORCE:
                    packageFinder = new BruteForceBestPackageFinder();
                    break;
                case KNAPSACK:
                    packageFinder = new KnapsackBestPackageFinder();
                    break;
                default:
                    throw new IllegalStateException("Invalid FindBestPackageStrategy: " + getFindBestPackageStrategy());
            }
        }
        return packageFinder.apply(packageSpecification);
    }
}
