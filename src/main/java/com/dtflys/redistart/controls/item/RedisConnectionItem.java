package com.dtflys.redistart.controls.item;

import com.dtflys.redistart.model.RedisConnection;

public class RedisConnectionItem extends RSBaseTreeItem {

    private final RedisConnection connection;

    public RedisConnectionItem(RedisConnection connection) {
        super(connection.getConnectionConfig().getName(), RSItemType.REDIS_CONNECTION);
        this.connection = connection;
    }

    @Override
    public RSItemStatus getItemStatus() {
        switch (connection.getStatus()) {
            case CONNECTING:
                return RSItemStatus.WAITING;
            default:
                return super.getItemStatus();
        }
    }

    public RedisConnection getConnection() {
        return connection;
    }


}
