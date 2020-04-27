package com.dtflys.redistart.controls.menu;

import com.dtflys.redistart.model.key.RSKeyType;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class RSKeyTypeMenuItem extends MenuItem {

    private final boolean useCheckBox;

    private CheckBox checkBox;

    private final RSKeyType keyType;

    private BooleanProperty bindProperty;

    public RSKeyTypeMenuItem(boolean useCheckBox, RSKeyType keyType) {
        super(keyType.name());
        this.useCheckBox = useCheckBox;
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        if (useCheckBox) {
            checkBox = new CheckBox();
            checkBox.setPrefWidth(12);
            checkBox.setPrefHeight(12);
            HBox.setMargin(checkBox, new Insets(0, 5, 0, 0));

        }
        this.keyType = keyType;

        ImageView imageView = new ImageView();
        imageView.setImage(keyType.getListIconImage());
        imageView.setFitWidth(18);
        imageView.setFitHeight(14);
        if (useCheckBox) {
            hBox.getChildren().add(checkBox);
        }
        hBox.getChildren().add(imageView);
        this.setGraphic(hBox);
        this.setText(keyType.name());
    }

    public RSKeyType getKeyType() {
        return keyType;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void unbind() {
        if (bindProperty != null && useCheckBox) {
            checkBox.selectedProperty().unbindBidirectional(bindProperty);
        }
    }

    public void bind(BooleanProperty booleanProperty) {
        if (useCheckBox) {
            checkBox.selectedProperty().bindBidirectional(booleanProperty);
            this.bindProperty = booleanProperty;
        }
    }
}
