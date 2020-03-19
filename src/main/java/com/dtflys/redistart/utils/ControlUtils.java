package com.dtflys.redistart.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ControlUtils {

    public static ChangeListener<String> getNumberChangeListener(TextField textField) {
        return new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        };
    }

    public static void numberField(TextField textField) {
        textField.textProperty().addListener(getNumberChangeListener(textField));
    }
}
