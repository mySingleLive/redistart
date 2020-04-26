package com.dtflys.redistart.controller.values;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dtflys.redistart.controls.editor.JSONEditor;
import com.dtflys.redistart.controls.editor.PlainTextEditor;
import com.dtflys.redistart.model.value.AbstractKeyValue;
import com.dtflys.redistart.model.value.StringValue;
import com.dtflys.redistart.model.valuemode.StringValueMode;
import com.dtflys.redistart.service.CommandService;
import com.dtflys.redistart.service.RediStartService;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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


    @Resource
    private CommandService commandService;

    @FXML
    private HBox stringValueToolbar;

    @FXML
    private StackPane stackPane;

    private ObjectPropertyBase<StringValue> stringValue = new SimpleObjectProperty<>(null);

    private StringProperty plainTextValue = new SimpleStringProperty("");

    private PlainTextEditor plainTextEditor = new PlainTextEditor();

    private JSONEditor jsonEditor = new JSONEditor();

    private VirtualizedScrollPane editorScrollPane;

    private StringProperty jsonText = new SimpleStringProperty();

    private StringValueMode plainTextMode;

    private StringValueMode jsonMode;


    private final ChangeListener<String> plainTextChangeListener = (observableValue, oldText, newText) -> {
        StringValue val = stringValue.get();
        if (val != null) {
            val.setValue(newText);
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        plainTextEditor.init();
        plainTextEditor.setStyle("-fx-font-size: 13");
        plainTextEditor.textProperty().addListener((observableValue, oldVal, newVal) -> {
            plainTextValue.set(newVal);
        });

        jsonEditor.init();
        jsonEditor.setStyle("-fx-font-size: 13");

        stackPane.getChildren().addAll(plainTextEditor.getEditorScrollPane(), jsonEditor.getEditorScrollPane());

        plainTextMode = new StringValueMode("Plain Text");
        plainTextMode.setOnSelect(mode -> {
            plainTextEditor.toFront();
        });

        jsonMode = new StringValueMode("JSON");
        jsonMode.setOnSelect(mode -> {
            jsonEditor.toFront();
        });

        rediStartService.setStringValueModeList(List.of(plainTextMode, jsonMode));
        rediStartService.stringValueModeProperty().addListener((observableValue, oldValueMode, newValueMode) -> {
            newValueMode.getOnSelect().accept(newValueMode);
        });

        plainTextValue.addListener((observableValue, oldText, newText) -> {
            if (newText != null) {
                formatJSONText(newText);
                plainTextEditor.setText(newText);
            }
        });

        jsonText.addListener((observableValue, oldText, newText) -> {
            if (newText != null) {
                jsonEditor.setText(newText);
            }
        });

        rediStartService.selectedKeyProperty().addListener((observableValue, oldKey, newKey) -> {
            if (newKey != null) {
                newKey.setOnValueChange((value) -> {
                    if (value != null) {
                        if (value instanceof StringValue) {
                            stringValue.set((StringValue) value);
                            plainTextValue.setValue(((StringValue) value).getValue());
                            selectValueMode(((StringValue) value).getValue());
                        }
                    }
                    setValueStatusText(value);
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

    public boolean formatJSONText(String text) {
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
            return true;

        } else {
            jsonText.set(text);
            return false;
        }
    }

    public void selectValueMode(String text) {
        if (formatJSONText(text)) {
            rediStartService.setStringValueMode(jsonMode);
        } else {
            rediStartService.setStringValueMode(plainTextMode);
        }
    }

}
