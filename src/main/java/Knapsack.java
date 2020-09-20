import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classical Knapsack algorithm implementation using a recursive approach with cached values.
 */
public class Knapsack {
    private static Logger LOG = Logger.getLogger("Knapsack");
    private final Product[] products;

    public Knapsack(Product[] products) {
        this.products = products;
    }

    public BitSet findMax(double maxWeight) {
        LOG.log(Level.FINE, "Max weight=" + maxWeight);
        LOG.log(Level.FINE, Arrays.toString(products));
        return findMaxCached(new Key(maxWeight, 0)).getIndexes();
    }

    private static class Key {
        private double maxWeight;
        private int start;

        public Key(double maxWeight, int start) {
            this.maxWeight = maxWeight;
            this.start = start;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Double.compare(key.maxWeight, maxWeight) == 0 &&
                start == key.start;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(maxWeight, start);
        }

        public double getMaxWeight() {
            return maxWeight;
        }

        public int getStart() {
            return start;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("maxWeight", maxWeight)
                .add("start", start)
                .toString();
        }
    }

    private static class Value {
        private double weight;
        private double price;
        private BitSet indexes;

        public Value(int length) {
            this.weight = 0;
            this.price = 0;
            this.indexes = new BitSet(length);
        }

        public Value(double weight, double price, BitSet indexes) {
            this.weight = weight;
            this.price = price;
            this.indexes = indexes;
        }

        public Value add(double weight, double price, int index) {
            BitSet newIndexes = (BitSet) indexes.clone();
            newIndexes.set(index);
            return new Value(this.weight + weight, this.price + price, newIndexes);
        }

        private static Comparator<Value> BEST_COMPARATOR =
            // compare first by price
            Comparator.comparingDouble(Value::getPrice)
                .thenComparing(
                    // and in case of equality compare by weight in reverse order
                    Comparator.comparingDouble(Value::getWeight).reversed()
                );

        public static Value max(Value v1, Value v2) {
            if (BEST_COMPARATOR.compare(v1, v2) < 0) {
                return v2;
            } else {
                return v1;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Value value = (Value) o;
            return Objects.equal(indexes, value.indexes);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(indexes);
        }

        public double getWeight() {
            return weight;
        }

        public double getPrice() {
            return price;
        }

        public BitSet getIndexes() {
            return indexes;
        }
    }

    private Map<Key,Value> cache = Maps.newHashMap();
    private Value findMaxCached(Key key) {
        if(cache.containsKey(key)){
            return cache.get(key);
        }else{
            Value max = findMax(key);
            cache.put(key, max);
            return max;
        }
    }
    private Value findMax(Key key) {
        // If we've gone through all the products, return
        if (key.getStart() == products.length) {
            LOG.log(Level.FINE, "findMax({0})=0", key);
            return new Value(products.length);
        }
        LOG.log(Level.FINE, "findMax({0})=?", key);

        // If the product weight is too big to fill the remaining space, skip it
        Value maxExcludingProduct = findMaxCached(new Key(key.getMaxWeight(), key.getStart() + 1));
        if (key.getMaxWeight() - products[key.getStart()].getWeight() < 0) {
            return maxExcludingProduct;
        }

        // Find the maximum of including and not including the current product
        Value maxIncludingProduct = findMaxCached(new Key(
            key.getMaxWeight() - products[key.getStart()].getWeight(),
            key.getStart() + 1))
            .add(products[key.getStart()].getWeight(),
                products[key.getStart()].getPrice(),
                key.getStart());
        Value max = Value.max(maxIncludingProduct, maxExcludingProduct);
        LOG.log(Level.FINE, "findMax({0})={1}", new Object[]{key, max.getPrice()});

        return max;
    }
}
