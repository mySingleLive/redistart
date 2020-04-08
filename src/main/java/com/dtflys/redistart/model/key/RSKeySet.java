package com.dtflys.redistart.model.key;

import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.command.RSCommandRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.client.protocol.decoder.ListScanResult;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RSKeySet {
    private final RedisConnection connection;
    private final RedisDatabase database;
    private volatile long startIndex = 0;
    private volatile long endIndex = 0;
    private final Lock lock = new ReentrantLock();
    private ObservableList<RSKey> keyList = FXCollections.observableArrayList();

    public RSKeySet(RedisDatabase database) {
        this.database = database;
        connection = database.getConnection();
    }

    private String scanScript() {
        String lua = "";
        return lua;
    }


    public void findNextPage() {
        int pageSize = connection.getConnectionConfig().getQueryPageSize();
        List<String> keys = new LinkedList<>();

        var record = new RSCommandRecord<ListScanResult<String>>(RedisCommands.SCAN, endIndex, "COUNT", pageSize)
                .onResult(listScanResult -> {
                    lock.lock();
                    try {
                        endIndex = listScanResult.getPos();
                    } finally {
                        lock.unlock();
                    }
        });
        database.sync(record);
    }


    public void clear() {
        keyList.clear();
        startIndex = 0;
        endIndex = 0;
    }

}
