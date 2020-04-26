package com.dtflys.redistart.model.valuemode;

import javafx.scene.Node;

import java.util.function.Consumer;

public class StringValueMode {

    private final String text;

    private Consumer<StringValueMode> onSelect;

    public StringValueMode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Consumer<StringValueMode> getOnSelect() {
        return onSelect;
    }

    public void setOnSelect(Consumer<StringValueMode> onSelect) {
        this.onSelect = onSelect;
    }
}
