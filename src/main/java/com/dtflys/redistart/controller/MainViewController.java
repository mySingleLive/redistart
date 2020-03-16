package com.dtflys.redistart.controller;

import com.dtflys.redistart.view.ConnectionSettingView;
import com.google.common.collect.Maps;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class MainViewController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuBar menuBar;

    @Resource
    private ConnectionSettingView connectionSettingView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onAddConnectionItemAction(ActionEvent actionEvent) {
        connectionSettingView.showStage(Modality.WINDOW_MODAL, Maps.newHashMap());
    }
}
