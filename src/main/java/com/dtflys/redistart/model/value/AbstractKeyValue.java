package com.dtflys.redistart.model.value;

import com.dtflys.redistart.model.key.RSKeyType;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class AbstractKeyValue {

    private StringProperty key = new SimpleStringProperty();

    private RSKeyType type;

    private LongProperty ttl = new SimpleLongProperty();

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
}
