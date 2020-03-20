package com.dtflys.redistart.controller;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.utils.ControlUtils;
import com.dtflys.redistart.utils.DialogUtils;
import com.dtflys.redistart.utils.RSController;
import com.dtflys.redistart.view.DialogView;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Map;
import java.util.function.Consumer;

public class ConnectionSettingController implements RSController {

    private final static String LITERAL_PWD_VISIBLE = "icm-eye";

    private final static String LITERAL_PWD_INVISIBLE = "icm-eye-blocked";

    private boolean modify = false;

    private ConnectionService connectionService;

    private RedisConnectionConfig connectionConfig;

    @FXML
    private VBox mainBox;

    @FXML
    private TextField txName;

    @FXML
    private TextField txRedisHost;

    @FXML
    private TextField txRedisPort;

    @FXML
    private PasswordField txAuth;

    @FXML
    private TextField txAuthVisible;

    @FXML
    private FontIcon iconPwdVisibility;

    @FXML
    private CheckBox cbUseSSL;

    @FXML
    private GridPane secGroup;

    @FXML
    private CheckBox cbUseSSH;

    @FXML
    private CustomTextField txSSHPort;

    @FXML
    private TabPane sshSettingTabView;

    @FXML
    private ComboBox cmbValidType;

    private DialogView dialogView;


    @Override
    public void init(Map<String, Object> args) {
        ControlUtils.numberField(txRedisPort);
        ControlUtils.numberField(txSSHPort);
        hideAuthText();
        secGroup.disableProperty().bind(cbUseSSL.selectedProperty().not());
        sshSettingTabView.disableProperty().bind(cbUseSSH.selectedProperty().not());
        cmbValidType.getItems().addAll("私钥", "密码");
    }

    private void hideAuthText() {
        txAuth.setVisible(true);
        txAuth.setMinWidth(Region.USE_COMPUTED_SIZE);
        txAuth.setPrefWidth(260);
        txAuthVisible.setVisible(false);
        txAuthVisible.setMinWidth(0);
        txAuthVisible.setPrefWidth(0);
        iconPwdVisibility.setIconLiteral(LITERAL_PWD_VISIBLE);

        txAuth.textProperty().unbind();
        txAuthVisible.textProperty().bind(txAuth.textProperty());
    }

    private void showAuthText() {
        txAuth.setVisible(false);
        txAuth.setMinWidth(0);
        txAuth.setPrefWidth(0);
        txAuthVisible.setVisible(true);
        txAuthVisible.setMinWidth(Region.USE_COMPUTED_SIZE);
        txAuthVisible.setPrefWidth(260);
        iconPwdVisibility.setIconLiteral(LITERAL_PWD_INVISIBLE);

        txAuthVisible.textProperty().unbind();
        txAuth.textProperty().bind(txAuthVisible.textProperty());
    }


    public void onOkAction(ActionEvent actionEvent) {
        RedisConnectionConfig connectionConfig = getConnectionConfig();
        connectionService.addConnection(connectionConfig);

        Stage stage = (Stage) txName.getScene().getWindow();
        stage.close();
    }

    public void onCancelAction(ActionEvent actionEvent) {
        Stage stage = (Stage) txName.getScene().getWindow();
        stage.close();
    }

    private String getAuthText() {
        if (txAuth.isVisible()) {
            return txAuth.getText();
        }
        return txAuthVisible.getText();
    }

    public void onPwdVisibilityClick(MouseEvent mouseEvent) {
        if (iconPwdVisibility.getIconLiteral().equals(LITERAL_PWD_INVISIBLE)) {
            iconPwdVisibility.setIconLiteral(LITERAL_PWD_VISIBLE);
            hideAuthText();
        } else {
            iconPwdVisibility.setIconLiteral(LITERAL_PWD_INVISIBLE);
            showAuthText();
        }
    }


    private RedisConnectionConfig getConnectionConfig() {
        String name = txName.getText().trim();
        String redisHost = txRedisHost.getText().trim();
        String redisPortText = txRedisPort.getText();
        Integer redisPort = null;
        try {
            redisPort = Integer.parseInt(redisPortText);
        } catch (Throwable th) {
        }
        String redisPassword = getAuthText();
        RedisConnectionConfig connectionConfig = new RedisConnectionConfig();
        if (StringUtils.isBlank(name)) {
            name = redisHost + ":" + redisPort;
        }
        connectionConfig.setName(name);
        connectionConfig.setRedisHost(redisHost);
        connectionConfig.setRedisPort(redisPort);
        connectionConfig.setRedisPassword(redisPassword);
        return connectionConfig;
    }


    public void onTestConnectionAction(ActionEvent actionEvent) {
        RedisConnectionConfig connectionConfig = getConnectionConfig();
        DialogUtils.showModalDialog(Map.of(
                "content", "",
                "width", 270,
                "showCancelButton", false,
                "onInit", (Consumer<DialogController>) dController -> {
                    HBox contentBox = dController.getContentBox();
                    contentBox.getChildren().clear();
                    Region leftRegion = new Region();
                    leftRegion.setPrefWidth(35);
                    JFXSpinner spinner = new JFXSpinner();
                    spinner.setPrefWidth(35);
                    spinner.setPrefHeight(35);
                    HBox.setMargin(spinner, new Insets(20, 0, 0, 0));
                    Label label = new Label();
                    label.setAlignment(Pos.CENTER_LEFT);
                    HBox.setMargin(label, new Insets(42, 0, 0, 30));
                    label.setText("请稍等...");
                    contentBox.getChildren().addAll(leftRegion, spinner, label);
                    JFXButton okButton = dController.getOkButton();
                    okButton.setDisable(true);

                    connectionService.startTestConnection(connectionConfig, result -> {
                        Platform.runLater(() -> {
                            spinner.setOpacity(0);
                            okButton.setDisable(false);
                            if (result) {
                                label.setText("连接成功");
                            } else {
                                label.setText("连接失败");
                            }
                        });
                    });
                }
        ));
    }
}
