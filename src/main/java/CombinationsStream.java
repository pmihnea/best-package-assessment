import com.google.common.base.Preconditions;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CombinationsStream {
    private final int n;
    private final BiFunction<BitSet, Integer, Boolean> canExtendCombination;

    public CombinationsStream(int n, BiFunction<BitSet, Integer, Boolean> canExtendCombination) {
        Preconditions.checkArgument(n > 0, "The number of elements should be bigger than 0.");
        Preconditions.checkNotNull(canExtendCombination, "The canExtendCombination function should not be null.");
        this.n = n;
        this.canExtendCombination = canExtendCombination;
    }

    public enum Strategy {
        RECURSIVE, ITERATIVE
    }
    private Strategy strategy = Strategy.ITERATIVE;

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Stream<BitSet> toBitSetStream() {
        switch (getStrategy()){
            case ITERATIVE: return combinationsStreamIterative();
            case RECURSIVE: return combinationsStreamRecursive(0);
            default: throw new IllegalStateException("Invalid CombinationsStream Strategy: " + getStrategy());
        }
    }

    private Stream<BitSet> combinationsStreamRecursive(int start) {
        if (start >= n) {
            return Stream.of(new BitSet(n));
        }
        return combinationsStreamRecursive(start + 1).flatMap(
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

    private Stream<BitSet> combinationsStreamIterative() {
        Iterator<BitSet> iterator = new Iterator<>() {
            BitSet combination = new BitSet(n);
            Integer last = -1;
            BitSet next = new BitSet(n); // empty combination

            @Override
            public boolean hasNext() {
                if (next != null) {
                    return true;
                }

                // find the next combination
                do {
                    if (last < n - 1) {
                        if (canExtendCombination.apply(combination, last + 1)) {
                            // extends the combination with a new element
                            combination.set(last = last + 1);
                            next = (BitSet) combination.clone();
                        } else {
                            last = last + 1;
                        }
                    } else {
                        // remove the elements from the combination until one has a valid successor
                        do {
                            last = combination.previousSetBit(n-1);
                            if(last < 0){
                                last = null;
                            }else {
                                combination.clear(last);
                            }
                        } while (last != null && last + 1 > n - 1);
                    }
                } while (last != null && next == null);

                return next != null;
            }

            @Override
            public BitSet next() {
                if (hasNext()) {
                    BitSet current = next;
                    next = null;
                    return current;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }
}
