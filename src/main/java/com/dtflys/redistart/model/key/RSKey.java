package com.dtflys.redistart.model.key;

import com.dtflys.redistart.model.database.RedisDatabase;

public class RSKey {

    private RedisDatabase database;

    private String key;

    private RSKeyType type;

    private Long ttl;

    public RSKey() {
    }

    public RedisDatabase getDatabase() {
        return database;
    }

    public void setDatabase(RedisDatabase database) {
        this.database = database;
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

    @Override
    public String toString() {
        return getKey();
    }
}
