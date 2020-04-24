package com.dtflys.redistart.controller.values;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dtflys.redistart.controls.editor.JSONEditor;
import com.dtflys.redistart.model.value.AbstractKeyValue;
import com.dtflys.redistart.model.value.StringValue;
import com.dtflys.redistart.service.CommandService;
import com.dtflys.redistart.service.RediStartService;
import com.jfoenix.controls.JFXTextArea;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import org.fxmisc.flowless.VirtualizedScrollPane;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class StringValueController implements Initializable {

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
                    setValueStatusText(newValue);
                });
                newKey.get(commandService);
            }
        });
    }

    public void setValueStatusText(AbstractKeyValue value) {
        if (value instanceof StringValue) {
            long byteSize = ((StringValue) value).getBytesSize();
            BigDecimal decimal = new BigDecimal(byteSize + "");
            String unit = "";
            if (byteSize >= 1024) {
                decimal = decimal.divide(new BigDecimal(1024), 2, RoundingMode.HALF_UP);
                if (byteSize >= 1024 * 1024) {
                    decimal = decimal.divide(new BigDecimal(1024), 2, RoundingMode.HALF_UP);
                    unit = "MB";
                } else {
                    decimal.setScale(2);
                    unit = "KB";
                }
            } else {
                unit = "B";
            }
            rediStartService.setValueStatusText("Value Size: " + decimal.toString() + unit);
        }
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

}
