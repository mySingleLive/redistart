package com.dtflys.redistart.model.page;

import com.dtflys.redistart.model.connection.RedisConnection;

public class RSKeysContentPage extends RSContentPage<RedisConnection> {

    private final RedisConnection connection;

    public RSKeysContentPage(RedisConnection connection) {
        super(KEYS_CONTENT_PAGE, true, true);
        this.connection = connection;
    }

    public RedisConnection getConnection() {
        return connection;
    }
}
