package com.dtflys.redistart.controller;

import com.dtflys.redistart.model.command.RSLuaRecord;
import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.model.value.AbstractKeyValue;
import com.dtflys.redistart.model.value.RSValueGetResult;
import com.dtflys.redistart.model.value.StringValue;
import com.dtflys.redistart.service.CommandService;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.service.RediStartService;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class StringValueViewController implements Initializable {

    @Resource
    private RediStartService rediStartService;

/*
    @Resource
    private ConnectionService connectionService;
*/

    @Resource
    private CommandService commandService;

    @FXML
    private JFXTextArea textArea;

    @FXML
    private Label lbStatus;

    @FXML
    private HBox valueModeBox;

    private ContextMenu valueModeMenu = new ContextMenu();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List.of("Plain Text", "JSON", "XML", "BIT (Binary)")
                .forEach(itemName -> {
                    MenuItem item = new MenuItem();
                    item.setText(itemName);
                    valueModeMenu.getItems().add(item);
                });

        rediStartService.selectedKeyProperty().addListener((observableValue, oldKey, newKey) -> {
            if (newKey != null) {
                textArea.textProperty().unbind();
                newKey.valueProperty().addListener((observableValue1, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (newValue instanceof StringValue) {
                            textArea.textProperty().bind(((StringValue) newValue).valueProperty());
                        }
                    }
                });
                newKey.get(commandService);
            }
        });
    }

    public void onValueModeClick(MouseEvent mouseEvent) {
        valueModeMenu.show(valueModeBox, Side.TOP, 0, 0);
    }
}
