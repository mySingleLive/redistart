package com.dtflys.redistart.controls;

import com.dtflys.redistart.utils.ResizeUtils;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class RSMovableListener {

    private Parent movable;

    private Stage stage;

    private double xOffset = 0;

    private double yOffset = 0;

    public RSMovableListener() {
    }

    public RSMovableListener(Stage stage) {
        this.stage = stage;
    }

    public void movable(Parent root) {
        this.movable = root;

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            event.consume();
            Stage stage = getStage();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public void resizable(Parent root) {
        ResizeUtils.addResizeListener(getStage(), root);
    }


    protected Stage getStage() {
        if (stage == null) {
            stage = (Stage) movable.getScene().getWindow();
        }
        return stage;
    }


}
