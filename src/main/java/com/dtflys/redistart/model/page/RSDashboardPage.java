package com.dtflys.redistart.model.page;

import com.dtflys.redistart.model.connection.RedisConnection;

public class RSDashboardPage extends RSContentPage<Object> {

    private final RedisConnection connection;

    public RSDashboardPage(RedisConnection connection) {
        super(DASHBOARD_PAGE, true, false);
        this.connection = connection;
    }

    public RedisConnection getConnection() {
        return connection;
    }
}
