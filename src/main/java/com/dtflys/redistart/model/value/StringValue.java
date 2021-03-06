package com.dtflys.redistart.model.value;

import com.dtflys.redistart.model.key.RSKeyType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringValue extends AbstractKeyValue<String> {

    private StringProperty value = new SimpleStringProperty();


    public StringValue(String key, RSKeyType type, Long ttl, String value) {
        super(key, type, ttl);
        this.value.addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null && onValueChange != null) {
                onValueChange.accept(newVal);
            }
        });
        setValue(value);
    }


    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }


    public long getBytesSize() {
        return value.get().getBytes().length;
    }
}
