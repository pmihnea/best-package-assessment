package com.pmihnea.bestpackage;

import java.util.Set;

/**
 * Encapsulates a parsed but yet not validated package specification composed of:<ul>
 * <li>max weight
 * <li>more products composed of: product number, weight, price
 * </ul>
 */
public class RawPackageSpecification extends BasePackageSpecification {

    public RawPackageSpecification(int lineNumber, double maxWeight, Set<Product> products) {
        super(lineNumber, maxWeight, products);
    }
}
