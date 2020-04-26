package com.dtflys.redistart.controls.editor;

import org.fxmisc.richtext.model.StyleSpan;

public class RSSpan<S> extends StyleSpan<S> {
    private int indent = 0;
    private S style;
    private int length;

    public RSSpan(S style, int length, int indent) {
        super(style, length);
        if (length < 0) {
            throw new IllegalArgumentException("StyleSpan's length cannot be negative");
        } else {
            this.style = style;
            this.length = length;
            this.indent = indent;
        }
    }

    public S getStyle() {
        return style;
    }

    public int getLength() {
        return length;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }
}
