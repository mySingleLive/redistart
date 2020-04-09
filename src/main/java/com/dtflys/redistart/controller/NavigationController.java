package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.item.RedisConnectionItem;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.service.ConnectionService;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.controlsfx.control.StatusBar;
import org.controlsfx.control.textfield.CustomTextField;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;

@FXMLController
public class NavigationController implements Initializable {

    @Resource
    private ConnectionService connectionService;

    @FXML
    private CustomTextField txSearchField;

    @FXML
    private JFXListView<RSKey> keyListView;

    @FXML
    private StatusBar keyStatusBar;

    private Map<RedisConnection, RedisConnectionItem> connectionItemMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectionService.setOnAfterAddConnection(this::addRedisConnectionItem);

        keyListView.setCellFactory(rsKeyListView -> {
            JFXListCell<RSKey> cell = new JFXListCell<>() {
                @Override
                protected void updateItem(RSKey item, boolean empty) {
                    super.updateItem(item, empty);
                }
            };

            return cell;
        });


        if (connectionService.getSelectedConnection() != null) {
            connectionService.getSelectedConnection().selectedDatabaseProperty().addListener((observableValue1, redisDatabase, newDatabase) -> {
                Platform.runLater(() -> {
                    keyListView.setItems(newDatabase.getKeySet().getKeyList());
                });
            });
            if (connectionService.getSelectedConnection().getSelectedDatabase() != null) {
                keyListView.setItems(connectionService.getSelectedConnection().getSelectedDatabase().getKeySet().getKeyList());
            }
        }

    }

    private void addRedisConnectionItem(RedisConnection connection) {
        RedisConnectionItem connItem = new RedisConnectionItem(connection);

    }


    public void onSearchCancel(MouseEvent mouseEvent) {
        txSearchField.setText("");
    }
}
