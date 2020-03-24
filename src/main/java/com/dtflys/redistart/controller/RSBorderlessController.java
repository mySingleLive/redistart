package com.dtflys.redistart.controller;

import com.dtflys.redistart.utils.ResizeUtils;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class RSBorderlessController {

    private Parent movable;

    private Stage stage;

    private double xOffset = 0;

    private double yOffset = 0;

    protected void movable(Parent root) {
        this.movable = root;

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            Stage stage = getStage();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    protected void resizable(Parent root) {
        ResizeUtils.addResizeListener(getStage(), root);
    }


    protected Stage getStage() {
        if (stage == null) {
            stage = (Stage) movable.getScene().getWindow();
        }
        return stage;
    }


}
