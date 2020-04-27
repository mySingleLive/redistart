package com.dtflys.redistart.controls;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;


public class RSToggleButton extends HBox {

    private ObjectPropertyBase<Node> graphic = new SimpleObjectProperty<>();
    private Label label = new Label();
    private BooleanProperty checked = new SimpleBooleanProperty(false);

    public RSToggleButton() {
        super();

        graphic.addListener((observableValue, oldNode, newNode) -> {
            List<Node> children = getChildren();
            children.clear();
            children.add(newNode);
            children.add(label);
        });

        ObservableList<String> styleClasses = getStyleClass();
        styleClasses.add("flat-button-box");
        checked.addListener((observableValue, oldVal, newVal) -> {
            pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), newVal);
        });
        setPadding(new Insets(0, 5, 0, 5));
        getChildren().add(label);

        setOnMouseClicked(event -> {
            setChecked(!isChecked());
        });
    }


    public Node getGraphic() {
        return graphic.get();
    }

    public ObjectPropertyBase<Node> graphicProperty() {
        return graphic;
    }

    public void setGraphic(Node graphic) {
        this.graphic.set(graphic);
    }

    public String getText() {
        return label.getText();
    }

    public StringProperty textProperty() {
        return label.textProperty();
    }

    public void setText(String text) {
        label.setText(text);
    }

    public boolean isChecked() {
        return checked.get();
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }
}
