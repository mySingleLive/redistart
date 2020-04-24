package com.dtflys.redistart.controls.menu;

import com.dtflys.redistart.model.key.RSKeyType;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class RSKeyTypeMenuItem extends MenuItem {

    private JFXCheckBox checkBox;

    private final RSKeyType keyType;

    private BooleanProperty bindProperty;

    public RSKeyTypeMenuItem(RSKeyType keyType) {
        super(keyType.name());
        HBox hBox = new HBox();
        checkBox = new JFXCheckBox();
        checkBox.setPrefWidth(12);
        checkBox.setPrefHeight(12);
        this.keyType = keyType;

        HBox.setMargin(checkBox, new Insets(0, 5, 0, 0));
        ImageView imageView = new ImageView();
        imageView.setImage(keyType.getListIconImage());
        imageView.setFitWidth(18);
        imageView.setFitHeight(14);
        hBox.getChildren().addAll(checkBox, imageView);
        this.setGraphic(hBox);
        this.setText(keyType.name());
    }

    public RSKeyType getKeyType() {
        return keyType;
    }

    public JFXCheckBox getCheckBox() {
        return checkBox;
    }

    public void unbind() {
        if (bindProperty != null) {
            checkBox.selectedProperty().unbindBidirectional(bindProperty);
        }
    }

    public void bind(BooleanProperty booleanProperty) {
        checkBox.selectedProperty().bindBidirectional(booleanProperty);
        this.bindProperty = booleanProperty;
    }
}
