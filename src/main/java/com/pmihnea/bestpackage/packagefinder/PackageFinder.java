package com.pmihnea.bestpackage.packagefinder;

import com.pmihnea.bestpackage.Package;
import com.pmihnea.bestpackage.PackageSpecification;

import java.util.Optional;
import java.util.function.Function;

/**
 * Describes the interface to find a package based on a package specification.
 */
public interface PackageFinder extends Function<PackageSpecification, Optional<Package>> {
}
