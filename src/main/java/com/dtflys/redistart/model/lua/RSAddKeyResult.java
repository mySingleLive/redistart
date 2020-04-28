package com.dtflys.redistart.model.lua;

import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.model.key.RSKeyType;

public class RSAddKeyResult {

    private RSResultCode code;

    private RSKeyType type;

    private String keyName;

    private RSKey key;

    public RSResultCode getCode() {
        return code;
    }

    public void setCode(RSResultCode code) {
        this.code = code;
    }

    public RSKeyType getType() {
        return type;
    }

    public void setType(RSKeyType type) {
        this.type = type;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public RSKey getKey() {
        return key;
    }

    public void setKey(RSKey key) {
        this.key = key;
    }
}
