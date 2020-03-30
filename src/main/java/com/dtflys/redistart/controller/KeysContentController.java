package com.dtflys.redistart.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class KeysContentController implements Initializable {

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private VBox leftBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SplitPane.setResizableWithParent(leftBox, Boolean.FALSE);
    }
}
