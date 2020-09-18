import com.google.common.base.Preconditions;

import java.util.BitSet;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class CombinationsStream {
    private final int n;
    private final BiFunction<BitSet, Integer, Boolean> canExtendCombination;

    public CombinationsStream(int n, BiFunction<BitSet, Integer, Boolean> canExtendCombination) {
        Preconditions.checkArgument(n > 0, "The number of elements should be bigger than 0.");
        Preconditions.checkNotNull(canExtendCombination, "The canExtendCombination function should not be null.");
        this.n = n;
        this.canExtendCombination = canExtendCombination;
    }

    public Stream<BitSet> toBitSetStream() {
        return combinations(0);
    }

    private Stream<BitSet> combinations(int start) {
        if (start >= n) {
            return Stream.of(new BitSet(n));
        }
        return combinations(start + 1).flatMap(
            combination -> canExtendCombination.apply(combination, start)
                ? Stream.of(combination, extendCombination(combination, start))
                : Stream.of(combination)
        );
    }

    private BitSet extendCombination(BitSet s, int e) {
        BitSet ns = new BitSet(s.size());
        ns.or(s);
        ns.set(e);
        return ns;
    }

}
