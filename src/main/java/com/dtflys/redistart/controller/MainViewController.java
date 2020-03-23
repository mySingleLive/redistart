package com.dtflys.redistart.controller;

import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.view.ConnectionSettingView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@FXMLController
public class MainViewController extends RSBorderlessController implements Initializable {


    @Resource
    private ConnectionService connectionService;

    @FXML
    private VBox appTitleBarBox;

    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private VBox leftBox;

    @FXML
    private HBox appMaximizeBox;

    @FXML
    private ImageView imgvMaximize;

    private Image maximizeImage;

    private Image maximizeRestoreImage;


    @Resource
    private ConnectionSettingView connectionSettingView;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.movable(appTitleBarBox);
        SplitPane.setResizableWithParent(leftBox, Boolean.FALSE);
        maximizeImage = imgvMaximize.getImage();
        maximizeRestoreImage = new Image( "/image/icons_max_restore_32px.png");
    }


    public void onAddConnectionItemAction(ActionEvent actionEvent) {
        connectionSettingView.showStage(Modality.WINDOW_MODAL, Map.of(
                "modify", false,
                "connectionService", connectionService));
    }


    private void toggleMaximizeWindow() {
        Stage stage = getStage();
        if (stage.isMaximized()) {
            getStage().setMaximized(false);
            imgvMaximize.setImage(maximizeImage);
        } else {
            getStage().setMaximized(true);
            imgvMaximize.setImage(maximizeRestoreImage);
        }
    }

    public void onAppCloseClicked(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void onAppMaximizeClicked(MouseEvent mouseEvent) {
        toggleMaximizeWindow();
    }

    public void onAppMinimizeClicked(MouseEvent mouseEvent) {
        getStage().setIconified(true);
    }

    public void onTitleBarClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            toggleMaximizeWindow();
        }
    }
}
