package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.item.RedisConnectionItem;
import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.service.ConnectionService;
import com.jfoenix.controls.JFXTreeView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class NavigationController implements Initializable {

    @Resource
    private ConnectionService connectionService;

    @FXML
    private CustomTextField txSearchField;

    @FXML
    private JFXTreeView treeView;

    private TreeItem<String> rootItem = new TreeItem<>("root");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        treeView.setRoot(rootItem);

        RedisConnectionConfig connectionConfig = new RedisConnectionConfig();
        connectionConfig.setName("Test 1");
        connectionConfig.setRedisHost("localhost");
        connectionConfig.setRedisPort(6379);
        connectionConfig.setUseSSL(false);
        connectionConfig.setUseSSH(false);
        RedisConnection connection = connectionService.addConnection(connectionConfig);
        RedisConnectionItem item1 = new RedisConnectionItem(connection);

        item1.getChildren().addAll(
                new TreeItem<>("isFirstOrder:CHN2078:M1002100095"),
                new TreeItem<>("POINT_EXCHANGE:SHOP_CART:2061193"),
                new TreeItem<>("POINT_EXCHANGE:SHOP_CART:PACK_ID"));

        rootItem.getChildren().addAll(item1);

    }

    public void onSearchCancel(MouseEvent mouseEvent) {
        txSearchField.setText("");
    }
}
