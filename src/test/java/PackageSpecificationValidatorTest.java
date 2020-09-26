import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackageSpecificationValidatorTest {
    @Test
    public void testInvalidInputLine() {
        PackageSpecificationValidationException ex = assertThrows(PackageSpecificationValidationException.class,
            () -> {
                RawPackageSpecification rawPackageSpecification = new RawPackageSpecification(
                    1,
                    101.0,
                    IntStream.rangeClosed(1, 16)
                        .mapToObj(value -> new Product(value, 101.0, 101.0))
                        .collect(Collectors.toSet()));
                new PackageSpecificationValidator().validateTokens(rawPackageSpecification);
            }
        );
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }
}
