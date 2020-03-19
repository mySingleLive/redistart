package com.dtflys.redistart.controller;

import com.dtflys.redistart.App;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.view.ConnectionSettingView;
import de.felixroske.jfxsupport.FXMLController;
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
import java.util.Map;
import java.util.ResourceBundle;

@FXMLController
public class MainViewController implements Initializable {

    @Resource
    private ConnectionService connectionService;

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
        var stage = App.getStage();
        SplitPane.setResizableWithParent(leftBox, Boolean.FALSE);
    }

    public void onAddConnectionItemAction(ActionEvent actionEvent) {
        connectionSettingView.showStage(Modality.WINDOW_MODAL, Map.of(
                "modify", false,
                "connectionService", connectionService));
    }
}
