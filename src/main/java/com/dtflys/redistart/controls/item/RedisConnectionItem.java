package com.dtflys.redistart.controls.item;

import com.dtflys.redistart.model.RedisConnection;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class RedisConnectionItem extends RSBaseTreeItem {

    private final RedisConnection connection;

    public RedisConnectionItem(RedisConnection connection) {
        super(connection.getConnectionConfig().getName(), RSItemType.REDIS_CONNECTION);
        this.connection = connection;

    }



    public RedisConnection getConnection() {
        return connection;
    }

}
