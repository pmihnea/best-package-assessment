package com.pmihnea.bestpackage;

import java.util.Set;

/**
 * Encapsulates a valid package specification composed of:<ul>
 * <li>max weight
 * <li>more products composed of: product number, weight, price
 * </ul>
 */
public class PackageSpecification extends BasePackageSpecification {

    public PackageSpecification(int lineNumber, double maxWeight, Set<Product> products) throws PackageSpecificationValidationException {
        super(lineNumber, maxWeight, products);
    }
}
