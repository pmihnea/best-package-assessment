import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.Objects;
import java.util.stream.Collectors;

public class KnapsackTest {
    @Test
    public void testFindMax(){
        Product[] products = {
            new Product(1, 10.0, 20.0),
            new Product(2, 15.0, 22.0),
            new Product(3, 8.0, 25.0)
        };
        BitSet max = new Knapsack(products).findMax(32);
        String result = max.stream().map(index -> products[index].getNumber()).sorted().mapToObj(Objects::toString).collect(Collectors.joining(","));
        Assertions.assertEquals("2,3", result);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testFindMaxEmptyProducts(){
        Product[] products = {};
        BitSet max = new Knapsack(products).findMax(32);
        String result = max.stream().map(index -> products[index].getNumber()).sorted().mapToObj(Objects::toString).collect(Collectors.joining(","));
        Assertions.assertEquals("", result);
    }
}
