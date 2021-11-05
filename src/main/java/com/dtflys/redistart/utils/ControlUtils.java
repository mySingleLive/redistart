package com.dtflys.redistart.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ControlUtils {

    public static ChangeListener<String> getNumberChangeListener(TextField textField) {
        return (observable, oldValue, newValue) -> {
            String val = textField.getText();
            boolean negative = false;
            if (newValue.startsWith("-")) {
                negative = true;
                val = val.substring(1);
            }
            if (!newValue.matches("(\\-)?[\\d]*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
                val = textField.getText();
            }
/*
            if (val.length() == 0) {
                val = "0";
            }
*/
            if (negative && !val.equals("0")) {
                val = "-" + val;
            }
            if (val.startsWith("0") && val.length() > 1) {
                int index = 0;
                for (; index < val.length(); index++) {
                    if (val.charAt(index) != '0') {
                        break;
                    }
                }
                if (index >= val.length()) {
                    index = val.length() - 1;
                }
                val = val.substring(index);
            }
            textField.setText(val);
        };
    }

    public static void numberField(TextField textField) {
        textField.textProperty().addListener(getNumberChangeListener(textField));
    }

    public static void numberField(TextField textField, String defaultText) {
        textField.textProperty().addListener(getNumberChangeListener(textField));
        textField.focusedProperty().addListener((observableValue, oldVal, newVal) -> {
            if (!newVal) {
                String val = textField.getText();
                if (val.isBlank() || val.equals("-")) {
                    textField.setText(defaultText);
                }
            }
        });
    }

}
