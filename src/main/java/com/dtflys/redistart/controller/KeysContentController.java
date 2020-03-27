package com.dtflys.redistart.controller;

import com.dtflys.redistart.utils.RSController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class KeysContentController implements RSController, Initializable {

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private VBox leftBox;

    @Override
    public void init(Map<String, Object> args) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SplitPane.setResizableWithParent(leftBox, Boolean.FALSE);
    }
}
