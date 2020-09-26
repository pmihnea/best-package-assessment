package com.pmihnea.bestpackage;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Encapsulates an optional package that is transformed into the desired output text line.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OutputLine {
    private final Optional<Package> optionalPackage;

    @Override
    public String toString() {
        if(optionalPackage.isPresent()){
            Set<Product> products = optionalPackage.get().getProducts();
            if(products.isEmpty()){
                return "-";
            }else{
               return products.stream().map(Product::getNumber).sorted().map(Object::toString).collect(Collectors.joining(","));
            }
        }else{
            return "-";
        }
    }

    public OutputLine(Optional<Package> optionalPackage) {
        this.optionalPackage = optionalPackage;
    }
}
