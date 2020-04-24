package com.dtflys.redistart.model.value;

public enum RSStringValueMode {
    PLAIN_TEXT("Plain Text"),
    JSON("JSON"),
    XML("XML"),
    BIT_BINARY("BIT (Binary)"),
    BIT_HEX("BIT (Hex)"),
    ;
    private final String text;

    RSStringValueMode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
