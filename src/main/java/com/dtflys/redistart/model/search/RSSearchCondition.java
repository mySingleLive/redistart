package com.dtflys.redistart.model.search;

import com.dtflys.redistart.model.key.RSKeyType;
import com.dtflys.redistart.model.ttl.RSTtlOperator;
import com.google.common.collect.Lists;
import javafx.beans.property.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class RSSearchCondition {

    private StringProperty pattern = new SimpleStringProperty();
    private ObjectPropertyBase<List<String>> types = new SimpleObjectProperty<>(Lists.newArrayList());
    private ObjectPropertyBase<RSTtlOperator> ttlOperator = new SimpleObjectProperty<>(null);
    private IntegerProperty ttl = new SimpleIntegerProperty();

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

    public RSTtlOperator getTtlOperator() {
        return ttlOperator.get();
    }

    public ObjectPropertyBase<RSTtlOperator> ttlOperatorProperty() {
        return ttlOperator;
    }

    public void setTtlOperator(RSTtlOperator ttlOperator) {
        this.ttlOperator.set(ttlOperator);
    }

    public int getTtl() {
        return ttl.get();
    }

    public IntegerProperty ttlProperty() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl.set(ttl);
    }
}
