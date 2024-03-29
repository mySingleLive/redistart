package com.dtflys.redistart.controller;

import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.utils.ControlUtils;
import com.dtflys.redistart.utils.Dialogs;
import com.dtflys.redistart.utils.IdGenerator;
import com.dtflys.redistart.utils.RSController;
import com.dtflys.redistart.view.DialogView;
import com.dtflys.redistart.view.model.ConnectionViewModel;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

public class ConnectionSettingController implements Initializable, RSController {

    private final static String LITERAL_PWD_VISIBLE = "icm-eye-blocked";

    private final static String LITERAL_PWD_INVISIBLE = "icm-eye";

    private boolean modify = false;

    @Autowired
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
    private TextField txSSHHost;

    @FXML
    private CustomTextField txSSHPort;

    @FXML
    private CustomTextField txSSHUsername;

    @FXML
    private TextField sshPrvKeyFile;

    @FXML
    private TabPane sshSettingTabView;

    @FXML
    private ComboBox cmbValidType;

    @FXML
    private CustomTextField txQueryPageSize;

    private DialogView dialogView;

    private ConnectionViewModel model = new ConnectionViewModel();

    public ConnectionViewModel getModel() {
        return model;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControlUtils.numberField(txRedisPort);
        ControlUtils.numberField(txSSHPort);
        ControlUtils.numberField(txQueryPageSize);

        NumberStringConverter numberStringConverter = new NumberStringConverter("#####");

        // 绑定属性

        txName.textProperty().bindBidirectional(model.nameProperty());
        txRedisHost.textProperty().bindBidirectional(model.redisHostProperty());
        txRedisPort.textProperty().bindBidirectional(model.redisPortProperty(), numberStringConverter);
        txAuth.textProperty().bindBidirectional(model.redisPasswordProperty());
        cbUseSSL.selectedProperty().bindBidirectional(model.isUseSSLProperty());
        cbUseSSH.selectedProperty().bindBidirectional(model.isUseSSHProperty());
        txSSHHost.textProperty().bindBidirectional(model.sshHostProperty());
        txSSHPort.textProperty().bindBidirectional(model.sshPortProperty(), numberStringConverter);
        txSSHUsername.textProperty().bindBidirectional(model.sshUsernameProperty());
        sshPrvKeyFile.textProperty().bindBidirectional(model.sshPrivateKeyFileProperty());
        txQueryPageSize.textProperty().bindBidirectional(model.queryPageSizeProperty(), numberStringConverter);

        hideAuthText();
        secGroup.disableProperty().bind(model.isUseSSLProperty().not());
        sshSettingTabView.disableProperty().bind(model.isUseSSHProperty().not());
        cmbValidType.getItems().addAll("公钥", "密码");


//        sshPublicKey.disableProperty().bind(cmbValidType.getSelectionModel().selectedIndexProperty().isEqualTo(0));

        if (modify && connectionConfig != null) {
            model.setName(connectionConfig.getName());
            model.setRedisHost(connectionConfig.getRedisHost());
            model.setRedisPort(connectionConfig.getRedisPort());
            model.setRedisPassword(connectionConfig.getRedisPassword());
            model.setIsUseSSL(connectionConfig.getIsUseSSL());
            model.setIsUseSSH(connectionConfig.getIsUseSSH());
            model.setSshHost(connectionConfig.getSshHost());
            model.setSshPort(connectionConfig.getSshPort());
            model.setSshUsername(connectionConfig.getSshUsername());
            model.setSshPrivateKeyFile(connectionConfig.getSshPrivateKeyFile());
            model.setSshPass(connectionConfig.getSshPass());
        }
    }

    @Override
    public void receiveArguments(Map<String, Object> args) {
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
        RedisConnectionConfig newConfig = getConnectionConfig();
        if (modify) {
            BeanUtils.copyProperties(newConfig, connectionConfig);
            connectionService.updateAllConnections();
        } else {
            connectionService.addConnection(newConfig);
        }

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

        // Redis 连接基本信息

        String name = model.getName();
        String redisHost = model.getRedisHost();
        Integer redisPort = model.getRedisPort();
        String redisPassword = getAuthText();
        RedisConnectionConfig connectionConfig = new RedisConnectionConfig();
        if (StringUtils.isBlank(name)) {
            name = redisHost + ":" + redisPort;
        }
        name = connectionService.getFixedConnectionName(name, modify);
        if (!modify) {
            Long id = IdGenerator.generateId();
            connectionConfig.setId(id + "");
        } else {
            connectionConfig.setId(this.connectionConfig.getId());
        }
        connectionConfig.setName(name);
        connectionConfig.setRedisHost(redisHost);
        connectionConfig.setRedisPort(redisPort);
        connectionConfig.setRedisPassword(redisPassword);
        connectionConfig.setCreateTime(new Date());
        connectionConfig.setIsUseSSL(model.isIsUseSSL());
        connectionConfig.setIsUseSSH(model.isIsUseSSH());
        connectionConfig.setSshUsername(model.getSshUsername());
        connectionConfig.setSshHost(model.getSshHost());
        connectionConfig.setSshPort(model.getSshPort());
        connectionConfig.setSshPrivateKeyFile(model.getSshPrivateKeyFile());
        connectionConfig.setSshPass(model.getSshPass());
        connectionConfig.setQueryPageSize(model.getQueryPageSize());
        return connectionConfig;
    }


    public void onTestConnectionAction(ActionEvent actionEvent) {
        RedisConnectionConfig connectionConfig = getConnectionConfig();
        Dialogs.applicationModal()
                .content("")
                .width(270)
                .showCancelButton(false)
                .onInit(dController -> {
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
                })
                .show();
    }

}
