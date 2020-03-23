package com.dtflys.redistart.controller;

import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class RSBorderlessController {

    private Parent movable;

    private Stage stage;

    private double xOffset = 0;

    private double yOffset = 0;

    protected void movable(Parent movable) {
        this.movable = movable;

        movable.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        movable.setOnMouseDragged(event -> {
            Stage stage = getStage();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

    }

    protected Stage getStage() {
        if (stage == null) {
            stage = (Stage) movable.getScene().getWindow();
        }
        return stage;
    }


}
