import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Encapsulates a product with a number, weight and price.
 */
public class Product {
    private Integer number;
    private Double weight;
    private Double price;

    public Product(Integer number, Double weight, Double price) {
        this.number = number;
        this.weight = weight;
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equal(number, product.number) &&
            Objects.equal(weight, product.weight) &&
            Objects.equal(price, product.price);
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
