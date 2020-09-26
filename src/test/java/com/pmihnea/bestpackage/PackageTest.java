package com.pmihnea.bestpackage;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageTest {
    @Test
    public void testComparatorDifferentPackagePrice(){
        Package aPackage = new Package(Sets.newHashSet(
            new Product(1, 10.0, 20.0),
            new Product(2, 10.0, 20.0)
        ));

        Package bPackage = new Package(Sets.newHashSet(
            new Product(3, 10.0, 20.0)
        ));

        assertTrue(Package.BEST_PACKAGE_COMPARATOR.compare(aPackage, bPackage) > 0);
    }

    @Test
    public void testComparatorSamePackagePriceDifferentWeight(){
        Package aPackage = new Package(Sets.newHashSet(
            new Product(1, 10.0, 20.0),
            new Product(2, 9.0, 20.0)
        ));

        Package bPackage = new Package(Sets.newHashSet(
            new Product(3, 10.0, 20.0),
            new Product(4, 10.0, 20.0)
        ));

        assertTrue(Package.BEST_PACKAGE_COMPARATOR.compare(aPackage, bPackage) > 0);
    }

    @Test
    public void testTotalPriceAndWeight(){
        Package aPackage = new Package(Sets.newHashSet(
            new Product(1, 10.0, 20.0),
            new Product(2, 9.0, 20.0)
        ));
        assertEquals(40.0, aPackage.getPrice());
        assertEquals(19.0, aPackage.getWeight());
    }
}
