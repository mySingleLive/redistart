package com.dtflys.redistart.controls.item;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionStatus;
import com.dtflys.redistart.model.RedisDatabase;
import javafx.scene.control.TreeView;

public class RedisDatabaseItem extends RSBaseTreeItem {

    private final RedisDatabase database;

    public RedisDatabaseItem(RedisDatabase database) {
        super(displayName(database), RSItemType.REDIS_DATABASE);
        this.database = database;
    }


    public static String displayName(RedisDatabase database) {
        return database.getName() + " (" + database.getSize() + ")";
    }


    @Override
    public void doAction(TreeView treeView) {
        System.out.println("Open Database: " + database.getIndex());
        if (!database.isOpened()) {
            database.openDatabase();
        }
    }

    public RSItemStatus getItemStatus() {
        return RSItemStatus.CLOSED;
    }

}
