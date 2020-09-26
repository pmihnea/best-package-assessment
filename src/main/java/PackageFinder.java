import java.util.Optional;
import java.util.function.Function;

/**
 * Describes the interface to find a package based on a package specification.
 */
public interface PackageFinder extends Function<PackageSpecification, Optional<Package>> {
}
