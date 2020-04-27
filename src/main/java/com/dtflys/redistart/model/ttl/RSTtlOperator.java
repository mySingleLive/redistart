package com.dtflys.redistart.model.ttl;

public enum  RSTtlOperator {

    NONE("", "none"),
    EQ("==", "="),
    GT(">", ">"),
    LT("<", "<"),
    GE(">=", "≥"),
    LE("<=", "≤")

    ;

    private final String opText;
    private final String displayText;

    RSTtlOperator(String opText, String displayText) {
        this.opText = opText;
        this.displayText = displayText;
    }

    public String getOpText() {
        return opText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
