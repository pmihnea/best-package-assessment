import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Encapsulates a product with a number, weight and price.
 */
public class Product {
    private final int number;
    private final double weight;
    private final double price;

    public Product(int number, double weight, double price) {
        this.number = number;
        this.weight = weight;
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public double getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return number == product.number &&
            Double.compare(product.weight, weight) == 0 &&
            Double.compare(product.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number, weight, price);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("number", number)
            .add("weight", weight)
            .add("price", price)
            .toString();
    }
}
