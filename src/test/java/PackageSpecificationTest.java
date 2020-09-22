import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageSpecificationTest {
    @Test
    public void testValidInputLine(){
        PackageSpecification packageSpecification = new PackageSpecification("81 : (1,53.38,€45) (2,88.62,€98)", 1);
        assertEquals(81.0, packageSpecification.getMaxWeight());
        Set<Product> expectedProducts = Sets.newHashSet(
            new Product(1, 53.38, 45.0),
            new Product(2, 88.62, 98.0)
        );
        assertEquals(expectedProducts, packageSpecification.getProducts());
    }

    @Test
    public  void testWrongDelimiters1() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 , (1,53.38,€45)", 1)
        );
        assertEquals(PackageSpecification.COLON_DELIMITER, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }
    @Test
    public  void testWrongDelimiters2(){
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 : ,1,53.38,€45)", 1)
        );
        assertEquals(PackageSpecification.PRODUCT_STRUCTURE, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }
    @Test
    public  void testWrongDelimiters3(){
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 : (1(53.38,€45)", 1)
        );
        assertEquals(PackageSpecification.PRODUCT_STRUCTURE, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }
    @Test
    public  void testWrongDelimiters4(){
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 : (1,53.38(€45)", 1)
        );
        assertEquals(PackageSpecification.PRODUCT_STRUCTURE, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }
    @Test
    public  void testWrongDelimiters5(){
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 : (1,53.38,(45)", 1)
        );
        assertEquals(PackageSpecification.PRODUCT_STRUCTURE, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }
    @Test
    public  void testWrongDelimiters6() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 : (1,53.38,€45X", 1)
        );
        assertEquals(PackageSpecification.PRODUCT_STRUCTURE, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public  void testWrongMaxWeight(){
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("XXX : ", 1)
        );
        assertEquals(PackageSpecification.MAX_WEIGHT, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public  void testWrongProductNumber(){
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 : (XXX,53.38,€45)", 1)
        );
        assertEquals(PackageSpecification.PRODUCT_NUMBER, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public  void testWrongProductWeight(){
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 : (1,XXX,€45)", 1)
        );
        assertEquals(PackageSpecification.PRODUCT_WEIGHT, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public  void testWrongProductPrice(){
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 : (1,53.38,€XXX)", 1)
        );
        assertEquals(PackageSpecification.PRODUCT_PRICE, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public  void testMissingProductPrice(){
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecification("81 : (1,53.38,€)", 1)
        );
        assertEquals(PackageSpecification.PRODUCT_STRUCTURE, ex.getTokenName());
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public  void testInvalidInputLine(){
        PackageSpecificationValidationException ex = assertThrows(PackageSpecificationValidationException.class,
            () -> {
                String inputLine = IntStream.rangeClosed(1,16).mapToObj(value -> " ("+value+",101.0,€101.0)").reduce("101.0 :", (s, s2) -> s + s2);
                new PackageSpecification(inputLine, 1);
            }
        );
        System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @ParameterizedTest
    @EnumSource(PackageSpecification.FindBestPackageStrategy.class)
    public  void testFindPackage1(PackageSpecification.FindBestPackageStrategy strategy){
        PackageSpecification packageSpecification = new PackageSpecification(81.0,
            new Product(1, 53.38, 45.0),
            new Product(2, 88.62, 98.0),
            new Product(3, 78.48, 3.0),
            new Product(4, 72.30, 76.0),
            new Product(5, 30.18, 9.0),
            new Product(6, 46.34, 48.0)
        );
        packageSpecification.setFindBestPackageStrategy(strategy);
        Optional<Package> aPackage = packageSpecification.findBestPackage();
        assertTrue(aPackage.isPresent());
        Set<Product> expectedProducts = Sets.newHashSet(
            new Product(4, 72.30, 76.0)
        );
        assertEquals(expectedProducts, aPackage.get().getProducts());
    }

    @ParameterizedTest
    @EnumSource(PackageSpecification.FindBestPackageStrategy.class)
    public  void testFindPackage2(PackageSpecification.FindBestPackageStrategy strategy){
        PackageSpecification packageSpecification = new PackageSpecification(8.0,
            new Product(1, 15.3, 34.0)
        );
        packageSpecification.setFindBestPackageStrategy(strategy);
        Optional<Package> aPackage = packageSpecification.findBestPackage();
        assertTrue(aPackage.isPresent());
        Set<Product> expectedProducts = Sets.newHashSet();
        assertEquals(expectedProducts, aPackage.get().getProducts());
    }

    @ParameterizedTest
    @EnumSource(PackageSpecification.FindBestPackageStrategy.class)
    public  void testFindPackage3(PackageSpecification.FindBestPackageStrategy strategy){
        PackageSpecification packageSpecification = new PackageSpecification(75.0,
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
        packageSpecification.setFindBestPackageStrategy(strategy);
        Optional<Package> aPackage = packageSpecification.findBestPackage();
        assertTrue(aPackage.isPresent());
        Set<Product> expectedProducts = Sets.newHashSet(
            new Product(2, 14.55, 74.0),
            new Product(7, 60.02, 74.0)
        );
        assertEquals(expectedProducts, aPackage.get().getProducts());
    }

    @ParameterizedTest
    @EnumSource(PackageSpecification.FindBestPackageStrategy.class)
    public  void testFindPackage4(PackageSpecification.FindBestPackageStrategy strategy){
        PackageSpecification packageSpecification = new PackageSpecification(56.0,
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
        packageSpecification.setFindBestPackageStrategy(strategy);
        Optional<Package> aPackage = packageSpecification.findBestPackage();
        assertTrue(aPackage.isPresent());
        Set<Product> expectedProducts = Sets.newHashSet(
            new Product(8, 19.36, 79.0),
            new Product(9, 6.76, 64.0)
        );
        assertEquals(expectedProducts, aPackage.get().getProducts());
    }
}
