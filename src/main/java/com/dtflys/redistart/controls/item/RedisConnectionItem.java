package com.dtflys.redistart.controls.item;

import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionStatus;
import javafx.scene.control.TreeView;

public class RedisConnectionItem extends RSBaseTreeItem {

    private final RedisConnection connection;

    public RedisConnectionItem(RedisConnection connection) {
        super(connection.getConnectionConfig().getName(), RSItemType.REDIS_CONNECTION);
        this.connection = connection;
    }

    @Override
    public void doAction(TreeView treeView) {
        if (connection.getStatus() == RedisConnectionStatus.CLOSED) {
            connection.openConnection();
        }
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
