package com.dtflys.redistart.model.search;

import com.dtflys.redistart.model.key.RSKeyType;
import com.google.common.collect.Lists;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class RSSearchInfo {

    private StringProperty pattern = new SimpleStringProperty();
    private ObjectPropertyBase<List<String>> types = new SimpleObjectProperty<>(Lists.newArrayList());

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
        return StringUtils.isNotBlank(getPattern()) || (!types.get().isEmpty() && types.get().size() < RSKeyType.values().length);
    }

    public StringProperty patternProperty() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern.set(pattern);
    }

    public List<String> getTypes() {
        return types.get();
    }

    public ObjectPropertyBase<List<String>> typesProperty() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types.set(types);
    }
}
