package com.dtflys.redistart.controller;

import com.dtflys.redistart.App;
import com.dtflys.redistart.view.ConnectionSettingView;
import com.google.common.collect.Maps;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class MainViewController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private VBox leftBox;

    @Resource
    private ConnectionSettingView connectionSettingView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Stage stage = App.getStage();
        SplitPane.setResizableWithParent(leftBox, Boolean.FALSE);
    }

    public void onAddConnectionItemAction(ActionEvent actionEvent) {
        connectionSettingView.showStage(Modality.WINDOW_MODAL, Maps.newHashMap());
    }
}
