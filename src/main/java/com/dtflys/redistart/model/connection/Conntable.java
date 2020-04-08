package com.dtflys.redistart.model.connection;

import com.dtflys.redistart.model.RedisConnectionConfig;

public abstract class Conntable {

    protected final RedisConnectionConfig connectionConfig;

    protected Conntable(RedisConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public RedisConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    protected abstract void doOpenConnection(int dbIndex);

    public abstract void closeConnection();
}
