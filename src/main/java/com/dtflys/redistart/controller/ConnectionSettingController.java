package com.dtflys.redistart.controller;

import com.dtflys.redistart.utils.RSController;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Map;

public class ConnectionSettingController implements RSController {

    private final static String LITERAL_PWD_VISIBLE = "icm-eye";

    private final static String LITERAL_PWD_INVISIBLE = "icm-eye-blocked";

    @FXML
    private TextField txName;

    @FXML
    private TextField txPort;

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
    private TabPane sshSettingTabView;

    @FXML
    private ComboBox cmbValidType;



    @Override
    public void init(Map<String, Object> args) {
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
}
