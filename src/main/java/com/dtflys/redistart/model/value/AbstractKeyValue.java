package com.dtflys.redistart.model.value;

import com.dtflys.redistart.model.key.RSKeyType;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.function.Consumer;

public abstract class AbstractKeyValue<T> {

    private StringProperty key = new SimpleStringProperty();

    private RSKeyType type;

    private LongProperty ttl = new SimpleLongProperty();

    protected Consumer<T> onValueChange;

    public AbstractKeyValue(String key, RSKeyType type, Long ttl) {
        setKey(key);
        this.type = type;
        setTtl(ttl);
    }

    public String getKey() {
        return key.get();
    }

    public StringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public RSKeyType getType() {
        return type;
    }

    public void setType(RSKeyType type) {
        this.type = type;
    }

    public long getTtl() {
        return ttl.get();
    }

    public LongProperty ttlProperty() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl.set(ttl);
    }

    public Consumer<T> getOnValueChange() {
        return onValueChange;
    }

    public void setOnValueChange(Consumer<T> onValueChange) {
        this.onValueChange = onValueChange;
    }
}
