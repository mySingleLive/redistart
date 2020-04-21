package com.dtflys.redistart.controls.editor;

public class RSSpan<S> {
    private S style;
    private int length;

    public RSSpan(S style, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("StyleSpan's length cannot be negative");
        } else {
            this.style = style;
            this.length = length;
        }
    }

    public S getStyle() {
        return style;
    }

    public int getLength() {
        return length;
    }

}
