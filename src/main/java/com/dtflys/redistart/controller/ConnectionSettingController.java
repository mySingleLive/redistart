package com.dtflys.redistart.controller;

import com.dtflys.redistart.utils.RSController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Map;

public class ConnectionSettingController implements RSController {

    @FXML
    private TextField txName;

    @FXML
    private TextField txPort;

    @Override
    public void init(Map<String, Object> args) {

    }

    public void onOkAction(ActionEvent actionEvent) {

    }

    public void onCancelAction(ActionEvent actionEvent) {
        Stage stage = (Stage) txName.getScene().getWindow();
        stage.close();
    }
}
