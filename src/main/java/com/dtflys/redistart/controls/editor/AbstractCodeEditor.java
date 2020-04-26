package com.dtflys.redistart.controls.editor;

import javafx.scene.layout.Region;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public abstract class AbstractCodeEditor extends CodeArea {

    protected VirtualizedScrollPane editorScrollPane;

    public void init () {
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        setEditable(true);
        setAutoScrollOnDragDesired(true);

        editorScrollPane = new VirtualizedScrollPane(this);
        editorScrollPane.getStyleClass().add("string-value-scroll-pane");
        editorScrollPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        editorScrollPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
    }

    public VirtualizedScrollPane getEditorScrollPane() {
        return editorScrollPane;
    }

    @Override
    public void toFront() {
        editorScrollPane.toFront();
    }

    @Override
    public void toBack() {
        editorScrollPane.toBack();
    }

    public void setText(String text) {
        replaceText(text);
    }
}
