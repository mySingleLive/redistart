package com.dtflys.redistart.controls.editor;

public class PlainTextEditor extends AbstractCodeEditor {

    @Override
    public void init() {
        super.init();
        editorScrollPane.getStylesheets().add("/css/plain-text.css");
        getStyleClass().setAll("string-value-plain-text");
    }
}
