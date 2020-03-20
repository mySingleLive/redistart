package com.dtflys.redistart.model.key;

import com.dtflys.redistart.model.RedisDatabase;

public class RSKey {

    private final RedisDatabase database;

    private final String key;

    private final RSKeyType type;

    public RSKey(RedisDatabase database, String key, RSKeyType type) {
        this.database = database;
        this.key = key;
        this.type = type;
    }

    public RedisDatabase getDatabase() {
        return database;
    }

    public String getKey() {
        return key;
    }

    public RSKeyType getType() {
        return type;
    }
}
