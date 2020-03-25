package com.dtflys.redistart.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class ConnectionManagerController implements Initializable {

    @FXML
    private TableView<String> connTableView;

    @FXML
    private TableColumn colConnName;

    @FXML
    private TableColumn colConnAddress;

    @FXML
    private TableColumn colConnSSH;

    @FXML
    private TableColumn colConnCreateTime;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
