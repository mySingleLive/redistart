package com.dtflys.redistart.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dtflys.redistart.controls.editor.JSONEditor;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.yaml.snakeyaml.serializer.Serializer;

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
    private HBox stringValueToolbar;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextArea textArea;

    @FXML
    private Label lbStatus;

    @FXML
    private HBox valueModeBox;

    private ContextMenu valueModeMenu = new ContextMenu();

    private JSONEditor jsonEditor = new JSONEditor();

    private VirtualizedScrollPane editorScrollPane;

    private StringProperty jsonText = new SimpleStringProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List.of("Plain Text", "JSON", "XML", "BIT (Binary)")
                .forEach(itemName -> {
                    MenuItem item = new MenuItem();
                    item.setText(itemName);
                    valueModeMenu.getItems().add(item);
                });

        jsonEditor.init();

        jsonEditor.setEditable(true);
        jsonEditor.setAutoScrollOnDragDesired(true);
        jsonEditor.getStyleClass().setAll("string-value-json-text");
        jsonEditor.setStyle("-fx-font-size: 13");

        editorScrollPane = new VirtualizedScrollPane(jsonEditor);
        editorScrollPane.getStyleClass().add("string-value-scroll-pane");
        stackPane.getChildren().add(editorScrollPane);
        editorScrollPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        editorScrollPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        editorScrollPane.toFront();

        rediStartService.selectedKeyProperty().addListener((observableValue, oldKey, newKey) -> {
            if (newKey != null) {
                textArea.textProperty().unbind();
                jsonEditor.replaceText("");
                newKey.valueProperty().addListener((observableValue1, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (newValue instanceof StringValue) {
                            textArea.textProperty().bind(((StringValue) newValue).valueProperty());
                            formatJSONText(((StringValue) newValue).valueProperty().get());
                            jsonEditor.replaceText(jsonText.get());
                        }
                    }
                });
                newKey.get(commandService);
            }
        });
    }

    public void formatJSONText(String text) {
        JSON json = null;
        try {
            json = JSON.parseObject(text);
        } catch (Throwable th) {
        }
        if (json == null) {
            try {
                json = JSON.parseArray(text);
            } catch (Throwable th) {
            }
        }
        if (json != null) {
            String output = JSON.toJSONString(json, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
//            output = output.replaceAll(":", ": ");
            output = output.replaceAll("\t", "    ");
            jsonText.set(output);
        } else {
            jsonText.set(text);
        }
    }

    public void onValueModeClick(MouseEvent mouseEvent) {
        valueModeMenu.show(valueModeBox, Side.TOP, 0, 0);
    }
}
