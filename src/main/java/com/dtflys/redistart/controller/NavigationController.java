package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.item.RSBaseTreeItem;
import com.dtflys.redistart.controls.item.RedisConnectionItem;
import com.dtflys.redistart.controls.item.RedisDatabaseItem;
import com.dtflys.redistart.event.RSEventHandler;
import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.model.RedisConnectionStatus;
import com.dtflys.redistart.model.RedisDatabase;
import com.dtflys.redistart.service.ConnectionService;
import com.jfoenix.controls.JFXTreeView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
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
import java.util.*;
import java.util.stream.Collectors;

@FXMLController
public class NavigationController implements Initializable {

    @Resource
    private ConnectionService connectionService;

    @FXML
    private CustomTextField txSearchField;

    @FXML
    private JFXTreeView treeView;

    private TreeItem<String> rootItem = new TreeItem<>("root");

    private Map<RedisConnection, RedisConnectionItem> connectionItemMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        treeView.setRoot(rootItem);
        connectionService.setOnAfterAddConnection(this::addRedisConnectionItem);

        treeView.setOnMouseClicked(event -> {
            // 双击树中元素
            if (event.getClickCount() == 2) {
                Object item = treeView.getSelectionModel().getSelectedItem();
                if (item instanceof RSBaseTreeItem) {
                    ((RSBaseTreeItem) item).doAction(treeView);
                }
            }
        });
    }

    private void addRedisConnectionItem(RedisConnection connection) {
        RedisConnectionItem connItem = new RedisConnectionItem(connection);

        connection.getOnBeforeOpenConnection().addHandler(conn -> {
            System.out.println("start opening connection");
            Platform.runLater(() -> {
                connItem.refresh(treeView);
            });
        });

        connection.getOnAfterOpenConnection().addHandler(conn -> {
            System.out.println("opened connection: " + conn.getConnectionConfig().getName());
            Platform.runLater(() -> {
                List<RedisDatabaseItem> databaseItems = conn.getDatabaseList()
                        .stream()
                        .map(database -> new RedisDatabaseItem(database))
                        .collect(Collectors.toList());
                connItem.getChildren().setAll(databaseItems);
                connItem.setExpanded(true);
                connItem.refresh(treeView);
            });
        });
        connectionItemMap.put(connection, connItem);
        rootItem.getChildren().add(connItem);
    }


    public void onSearchCancel(MouseEvent mouseEvent) {
        txSearchField.setText("");
    }
}
