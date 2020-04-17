package com.dtflys.redistart.model.search;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

public class RSSearchInfo {

    private StringProperty pattern = new SimpleStringProperty();

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
}
