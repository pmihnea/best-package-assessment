public class PackageSpecificationParsingException extends PackageSpecificationBaseException {
    private final String tokenName;
    private final String restLine;

    public PackageSpecificationParsingException(int lineNumber, String tokenName, String restLine, Throwable throwable) {
        super("Invalid '" + tokenName + "' found on line " + lineNumber + " while parsing the rest of the line '" + restLine + "'.", lineNumber, throwable);
        this.tokenName = tokenName;
        this.restLine = restLine;
    }

    public String getTokenName() {
        return tokenName;
    }

    public String getRestLine() {
        return restLine;
    }
}
