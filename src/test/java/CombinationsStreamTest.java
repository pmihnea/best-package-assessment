import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CombinationsStreamTest {
    @Test
    public void testCombinationsOf3(){
        CombinationsStream combinationsStream = new CombinationsStream(3,
            (bitSet, i) -> true
        );
        Set<String> combinations = combinationsStream.toBitSetStream().map(BitSet::toString).collect(Collectors.toSet());
        Set<String> expected = Set.of("{}", "{0}", "{1}", "{0, 1}", "{2}", "{0, 2}", "{1, 2}", "{0, 1, 2}");
        Assertions.assertEquals(expected, combinations);
    }

    @Test
    public void testCombinationsWithCondition(){
        CombinationsStream combinationsStream = new CombinationsStream(4,
            (bitSet, i) -> bitSet.stream().reduce(Integer::sum).orElse(0) + i <= 3
        );
        Set<String> combinations = combinationsStream.toBitSetStream().map(BitSet::toString).collect(Collectors.toSet());
        Set<String> expected = Set.of("{}", "{3}", "{1}", "{2}", "{0, 2}", "{0}", "{0, 1}", "{0, 1, 2}", "{1, 2}", "{0, 3}");
        Assertions.assertEquals(expected, combinations);
    }

    @Test
    public void testInvalidArguments(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CombinationsStream(0, (bitSet, integer) -> true));
        Assertions.assertThrows(NullPointerException.class, () -> new CombinationsStream(1, null));
    }

    @Test
    public void testManyCombinations(){
        CombinationsStream combinationsStream = new CombinationsStream(15,
            (bitSet, i) -> true
        );
        Assertions.assertEquals(32768, combinationsStream.toBitSetStream().count());

        CombinationsStream combinationsStream2 = new CombinationsStream(30,
            (bitSet, i) -> bitSet.stream().reduce(Integer::sum).orElse(0) + i <= 15
        );
        Assertions.assertEquals(274, combinationsStream2.toBitSetStream().count());
    }
}
