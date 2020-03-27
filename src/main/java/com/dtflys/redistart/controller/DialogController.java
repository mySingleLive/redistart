package com.dtflys.redistart.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class DialogController extends RSBorderlessController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label lbTitle;

    @FXML
    private Label lbContent;

    @FXML
    private HBox contentBox;

    @FXML
    private HBox buttonsBox;

    @FXML
    private JFXButton okButton;

    @FXML
    private JFXButton cancelButton;

    private Stage stage;

    private double xOffset = 0;

    private double yOffset = 0;

    private Boolean showOkButton = true;

    private Boolean showCancelButton = true;

    private String title;

    private String content;

    private Integer width;

    private Integer height;

    private Consumer<DialogController> onInit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.movable(mainPane);
        lbTitle.setText(Optional.of(title).get());
        lbContent.setText(Optional.of(content).get());
        if (width != null) {
            mainPane.setPrefWidth(width);
        }
        if (height != null) {
            mainPane.setPrefHeight(height);
        }

        if (showOkButton != null && !showOkButton) {
            buttonsBox.getChildren().remove(okButton);
        }

        if (showCancelButton != null && !showCancelButton) {
            buttonsBox.getChildren().remove(cancelButton);
        }


        if (onInit != null) {
            onInit.accept(this);
        }
    }



    public BorderPane getMainPane() {
        return mainPane;
    }

    public HBox getContentBox() {
        return contentBox;
    }

    public Label getContentLabel() {
        return lbContent;
    }

    public JFXButton getOkButton() {
        return okButton;
    }

    public JFXButton getCancelButton() {
        return cancelButton;
    }

    public void onCloseAction(MouseEvent mouseEvent) {
        getStage().close();
    }


    public void onCancelAction(ActionEvent actionEvent) {
        getStage().close();
    }

    public void onOkAtion(ActionEvent actionEvent) {
        getStage().close();
    }

}
