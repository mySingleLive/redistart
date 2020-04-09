package com.dtflys.redistart.model.key;

public enum RSKeyType {
    string,
    list,
    hash,
    set,
    zset
    ;

    public RSKeyType findByName(String name) {
        return RSKeyType.valueOf(name.toUpperCase());
    }

}
