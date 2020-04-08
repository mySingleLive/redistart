package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.item.RedisConnectionItem;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.service.ConnectionService;
import com.jfoenix.controls.JFXListView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
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
    private JFXListView keyListView;

    @FXML
    private StatusBar keyStatusBar;

    private Map<RedisConnection, RedisConnectionItem> connectionItemMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectionService.setOnAfterAddConnection(this::addRedisConnectionItem);
    }

    private void addRedisConnectionItem(RedisConnection connection) {
        RedisConnectionItem connItem = new RedisConnectionItem(connection);

    }


    public void onSearchCancel(MouseEvent mouseEvent) {
        txSearchField.setText("");
    }
}
