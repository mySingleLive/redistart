package com.dtflys.redistart.model.value;

import com.dtflys.redistart.model.key.RSKeyType;

public class RSValueGetResult {

    private Integer status;

    private String key;

    private RSKeyType type;

    private Long ttl;

    private String value;

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public RSKeyType getType() {
        return type;
    }

    public void setType(RSKeyType type) {
        this.type = type;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
