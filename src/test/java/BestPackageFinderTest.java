import com.google.common.collect.Sets;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BestPackageFinderTest {

    @ParameterizedTest
    @EnumSource(BestPackageFinder.FindBestPackageStrategy.class)
    public void testFindPackage1(BestPackageFinder.FindBestPackageStrategy strategy) {
        PackageSpecification packageSpecification = new PackageSpecification(1, 81.0,
            new Product(1, 53.38, 45.0),
            new Product(2, 88.62, 98.0),
            new Product(3, 78.48, 3.0),
            new Product(4, 72.30, 76.0),
            new Product(5, 30.18, 9.0),
            new Product(6, 46.34, 48.0)
        );

        BestPackageFinder bestPackageFinder = new BestPackageFinder().withFindBestPackageStrategy(strategy);
        Optional<Package> aPackage = bestPackageFinder.findBestPackage(packageSpecification);
        assertTrue(aPackage.isPresent());
        Set<Product> expectedProducts = Sets.newHashSet(
            new Product(4, 72.30, 76.0)
        );
        assertEquals(expectedProducts, aPackage.get().getProducts());
    }

    @ParameterizedTest
    @EnumSource(BestPackageFinder.FindBestPackageStrategy.class)
    public void testFindPackage2(BestPackageFinder.FindBestPackageStrategy strategy) {
        PackageSpecification packageSpecification = new PackageSpecification(1, 8.0,
            new Product(1, 15.3, 34.0)
        );
        BestPackageFinder bestPackageFinder = new BestPackageFinder().withFindBestPackageStrategy(strategy);
        Optional<Package> aPackage = bestPackageFinder.findBestPackage(packageSpecification);
        assertTrue(aPackage.isPresent());
        Set<Product> expectedProducts = Sets.newHashSet();
        assertEquals(expectedProducts, aPackage.get().getProducts());
    }

    @ParameterizedTest
    @EnumSource(BestPackageFinder.FindBestPackageStrategy.class)
    public void testFindPackage3(BestPackageFinder.FindBestPackageStrategy strategy) {
        PackageSpecification packageSpecification = new PackageSpecification(1, 75.0,
            new Product(1, 85.31, 29.0),
            new Product(2, 14.55, 74.0),
            new Product(3, 3.98, 16.0),
            new Product(4, 26.24, 55.0),
            new Product(5, 63.69, 52.0),
            new Product(6, 76.25, 75.0),
            new Product(7, 60.02, 74.0),
            new Product(8, 93.18, 35.0),
            new Product(9, 89.95, 78.0)
        );
        BestPackageFinder bestPackageFinder = new BestPackageFinder().withFindBestPackageStrategy(strategy);
        Optional<Package> aPackage = bestPackageFinder.findBestPackage(packageSpecification);
        assertTrue(aPackage.isPresent());
        Set<Product> expectedProducts = Sets.newHashSet(
            new Product(2, 14.55, 74.0),
            new Product(7, 60.02, 74.0)
        );
        assertEquals(expectedProducts, aPackage.get().getProducts());
    }

    @ParameterizedTest
    @EnumSource(BestPackageFinder.FindBestPackageStrategy.class)
    public void testFindPackage4(BestPackageFinder.FindBestPackageStrategy strategy) {
        PackageSpecification packageSpecification = new PackageSpecification(1, 56.0,
            new Product(1, 90.72, 13.0),
            new Product(2, 33.80, 40.0),
            new Product(3, 43.15, 10.0),
            new Product(4, 37.97, 16.0),
            new Product(5, 46.81, 36.0),
            new Product(6, 48.77, 79.0),
            new Product(7, 81.80, 45.0),
            new Product(8, 19.36, 79.0),
            new Product(9, 6.76, 64.0)
        );
        BestPackageFinder bestPackageFinder = new BestPackageFinder().withFindBestPackageStrategy(strategy);
        Optional<Package> aPackage = bestPackageFinder.findBestPackage(packageSpecification);
        assertTrue(aPackage.isPresent());
        Set<Product> expectedProducts = Sets.newHashSet(
            new Product(8, 19.36, 79.0),
            new Product(9, 6.76, 64.0)
        );
        assertEquals(expectedProducts, aPackage.get().getProducts());
    }
}
