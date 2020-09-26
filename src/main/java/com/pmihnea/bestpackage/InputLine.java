package com.pmihnea.bestpackage;

public class InputLine {
    // line identification
    private final int lineNumber;
    private final String stringLine;

    public int getLineNumber() {
        return lineNumber;
    }

    public String getStringLine() {
        return stringLine;
    }

    public InputLine(int lineNumber, String stringLine) {
        this.lineNumber = lineNumber;
        this.stringLine = stringLine;
    }
}
