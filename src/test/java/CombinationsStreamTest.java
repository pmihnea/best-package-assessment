import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.BitSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CombinationsStreamTest {
    @ParameterizedTest
    @EnumSource(CombinationsStream.Strategy.class)
    public void testCombinationsOf3(CombinationsStream.Strategy strategy) {
        CombinationsStream combinationsStream = new CombinationsStream(3,
            (bitSet, i) -> true
        );
        combinationsStream.setStrategy(strategy);
        Set<String> combinations = combinationsStream.toBitSetStream().map(BitSet::toString).collect(Collectors.toSet());
        Set<String> expected = Set.of("{}", "{0}", "{1}", "{0, 1}", "{2}", "{0, 2}", "{1, 2}", "{0, 1, 2}");
        Assertions.assertEquals(expected, combinations);
    }

    @ParameterizedTest
    @EnumSource(CombinationsStream.Strategy.class)
    public void testCombinationsWithCondition1(CombinationsStream.Strategy strategy) {
        CombinationsStream combinationsStream = new CombinationsStream(4,
            (bitSet, i) -> bitSet.stream().reduce(Integer::sum).orElse(0) + i <= 3
        );
        combinationsStream.setStrategy(strategy);
        Set<String> combinations = combinationsStream.toBitSetStream().map(BitSet::toString).collect(Collectors.toSet());
        Set<String> expected = Set.of("{}", "{3}", "{1}", "{2}", "{0, 2}", "{0}", "{0, 1}", "{0, 1, 2}", "{1, 2}", "{0, 3}");
        Assertions.assertEquals(expected, combinations);
    }

    @ParameterizedTest
    @EnumSource(CombinationsStream.Strategy.class)
    public void testCombinationsWithCondition2(CombinationsStream.Strategy strategy) {
        CombinationsStream combinationsStream = new CombinationsStream(4,
            (bitSet, i) -> (i != 1 && i != 3)
        );
        combinationsStream.setStrategy(strategy);
        Set<String> combinations = combinationsStream.toBitSetStream().map(BitSet::toString).collect(Collectors.toSet());
        Set<String> expected = Set.of("{}", "{0}", "{2}", "{0, 2}");
        Assertions.assertEquals(expected, combinations);
    }

    @ParameterizedTest
    @EnumSource(CombinationsStream.Strategy.class)
    public void testCombinationsWithConditionAlwaysFalse(CombinationsStream.Strategy strategy) {
        CombinationsStream combinationsStream = new CombinationsStream(4,
            (bitSet, i) -> false
        );
        combinationsStream.setStrategy(strategy);
        Set<String> combinations = combinationsStream.toBitSetStream().map(BitSet::toString).collect(Collectors.toSet());
        Set<String> expected = Set.of("{}");
        Assertions.assertEquals(expected, combinations);
    }

    @Test
    public void testInvalidArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CombinationsStream(0, (bitSet, integer) -> true));
        Assertions.assertThrows(NullPointerException.class, () -> new CombinationsStream(1, null));
    }

    @ParameterizedTest
    @EnumSource(CombinationsStream.Strategy.class)
    public void testManyCombinations(CombinationsStream.Strategy strategy) {
        CombinationsStream combinationsStream1 = new CombinationsStream(15,
            (bitSet, i) -> true
        );
        combinationsStream1.setStrategy(strategy);
        Assertions.assertEquals(32768, combinationsStream1.toBitSetStream().count());

        CombinationsStream combinationsStream2 = new CombinationsStream(30,
            (bitSet, i) -> bitSet.stream().reduce(Integer::sum).orElse(0) + i <= 15
        );
        combinationsStream2.setStrategy(strategy);
        Assertions.assertEquals(274, combinationsStream2.toBitSetStream().count());
    }

}
