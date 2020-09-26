import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackageSpecificationReaderTest {
    @Test
    public void testValidInputLine() {
        RawPackageSpecification packageSpecification = new PackageSpecificationReader().readTokens(new InputLine(1, "81 : (1,53.38,€45) (2,88.62,€98)"));
        assertEquals(81.0, packageSpecification.getMaxWeight());
        Set<Product> expectedProducts = Sets.newHashSet(
            new Product(1, 53.38, 45.0),
            new Product(2, 88.62, 98.0)
        );
        assertEquals(expectedProducts, packageSpecification.getProducts());
    }

    @Test
    public void testWrongDelimiters1() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 , (1,53.38,€45)"))
        );
        assertEquals(InputLineTokens.LINE_STRUCTURE, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testWrongDelimiters2() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 : ,1,53.38,€45)"))
        );
        assertEquals(InputLineTokens.LINE_STRUCTURE, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testWrongDelimiters3() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 : (1(53.38,€45)"))
        );
        assertEquals(InputLineTokens.LINE_STRUCTURE, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testWrongDelimiters4() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 : (1,53.38(€45)"))
        );
        assertEquals(InputLineTokens.LINE_STRUCTURE, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testWrongDelimiters5() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 : (1,53.38,(45)"))
        );
        assertEquals(InputLineTokens.LINE_STRUCTURE, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testWrongDelimiters6() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 : (1,53.38,€45X"))
        );
        assertEquals(InputLineTokens.LINE_STRUCTURE, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testWrongMaxWeight() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "XXX : (1,53.38,€45)"))
        );
        assertEquals(InputLineTokens.MAX_WEIGHT, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testWrongProductNumber() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 : (XXX,53.38,€45)"))
        );
        assertEquals(InputLineTokens.PRODUCT_NUMBER, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testWrongProductWeight() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 : (1,XXX,€45)"))
        );
        assertEquals(InputLineTokens.PRODUCT_WEIGHT, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testWrongProductPrice() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 : (1,53.38,€XXX)"))
        );
        assertEquals(InputLineTokens.PRODUCT_PRICE, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }

    @Test
    public void testMissingProductPrice() {
        PackageSpecificationParsingException ex = assertThrows(PackageSpecificationParsingException.class,
            () -> new PackageSpecificationReader().readTokens(new InputLine(1, "81 : (1,53.38,€)"))
        );
        assertEquals(InputLineTokens.LINE_STRUCTURE, ex.getTokenName());
        // System.out.println(ex.getMessage() + " Cause: " + ex.getCause());
    }


}
