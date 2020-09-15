public class PackageSpecificationValidationException extends PackageSpecificationBaseException {
    public PackageSpecificationValidationException(String message, int lineNumber) {
        super("Invalid values found on line " + lineNumber + " :"+ System.lineSeparator() + message, lineNumber);
    }
}
