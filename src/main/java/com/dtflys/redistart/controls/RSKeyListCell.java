package com.dtflys.redistart.controls;

import com.dtflys.redistart.model.key.RSKey;
import com.jfoenix.controls.JFXListCell;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class RSKeyListCell extends JFXListCell<RSKey> {

    private ImageView typeIconView = new ImageView();

    private Label ttlLabel = new Label();

    public RSKeyListCell() {
        typeIconView.setFitWidth(20);
        typeIconView.setFitHeight(20 * 0.75);
    }

    @Override
    protected void updateItem(RSKey item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            if (item.getType() != null) {
                typeIconView.setImage(item.getType().getListIconImage());
            } else {
                typeIconView.setImage(null);
            }
            ttlLabel.setText("ttl: " + item.getTtl());
            this.setText(item.getKey());
            this.setGraphic(typeIconView);
        }
    }

}
