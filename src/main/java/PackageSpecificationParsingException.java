public class PackageSpecificationParsingException extends PackageSpecificationBaseException {
    private String actualToken;
    private String tokenName;

    public PackageSpecificationParsingException(int lineNumber, String tokenName, String actualToken) {
        super("Invalid '" + tokenName + "' found on line " + lineNumber + " while parsing the token '" + actualToken + "'.", lineNumber);
        this.tokenName = tokenName;
        this.actualToken = actualToken;
    }

    public String getActualToken() {
        return actualToken;
    }

    public String getTokenName() {
        return tokenName;
    }
}
