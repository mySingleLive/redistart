package com.dtflys.redistart.model.value;

import com.dtflys.redistart.model.key.RSKeyType;

import java.util.List;

public class ValueBuilder {

    private String key;

    private RSKeyType type;

    private Long ttl;

    private Object value;


    public String getKey() {
        return key;
    }

    public ValueBuilder key(String key) {
        this.key = key;
        return this;
    }

    public RSKeyType getType() {
        return type;
    }

    public ValueBuilder type(RSKeyType type) {
        this.type = type;
        return this;
    }

    public Long getTtl() {
        return ttl;
    }

    public ValueBuilder ttl(Long ttl) {
        this.ttl = ttl;
        return this;
    }

    public ValueBuilder value(Object value) {
        this.value = value;
        return this;
    }


    public AbstractKeyValue build() {
        switch (type) {
            case string:
                String str = null;
                if (value != null) {
                    str = value.toString();
                } else {
                    str = "";
                }
                StringValue stringValue = new StringValue(key, type, ttl, str);
                return stringValue;
            default:
                return null;
        }
    }
}
