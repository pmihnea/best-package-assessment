public class PackageSpecificationBaseException extends RuntimeException {
    protected final int lineNumber;

    public PackageSpecificationBaseException(String message, int lineNumber) {
        super(message);
        this.lineNumber = lineNumber;
    }

    public PackageSpecificationBaseException(String message, int lineNumber, Throwable throwable) {
        super(message, throwable);
        this.lineNumber = lineNumber;
    }
    public int getLineNumber() {
        return lineNumber;
    }
}
