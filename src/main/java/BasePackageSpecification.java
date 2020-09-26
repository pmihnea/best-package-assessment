import java.util.Set;

public abstract class BasePackageSpecification {
    // line identification
    protected final int lineNumber;
    // package specification fields
    protected final double maxWeight;
    protected final Set<Product> products;

    public BasePackageSpecification(int lineNumber, double maxWeight, Set<Product> products) {
        this.lineNumber = lineNumber;
        this.maxWeight = maxWeight;
        this.products = products;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
