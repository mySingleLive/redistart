package com.dtflys.redistart.model.key;

public enum RSKeyType {

    STRING,
    LIST,
    HASH,
    SET,
    ZSET

    ;

    public RSKeyType findByName(String name) {
        return RSKeyType.valueOf(name.toUpperCase());
    }

}
