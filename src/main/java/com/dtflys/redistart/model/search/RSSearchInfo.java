package com.dtflys.redistart.model.search;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RSSearchInfo {

    private StringProperty pattern = new SimpleStringProperty();
    private ObservableList<String> types = FXCollections.observableArrayList();

    public String getPattern() {
        String str = pattern.get();
        if (StringUtils.isNotBlank(str)) {
            str = str.trim();
            char endCh = str.charAt(str.length() - 1);
            if (endCh != '*') {
                return str + "*";
            }
        }
        return str;
    }

    public boolean isSearchMode() {
        return StringUtils.isNotBlank(getPattern());
    }

    public StringProperty patternProperty() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern.set(pattern);
    }

    public ObservableList<String> getTypes() {
        return types;
    }

    public void setTypes(ObservableList<String> types) {
        this.types = types;
    }
}
