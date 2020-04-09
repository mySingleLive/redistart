package com.dtflys.redistart.model.key;

import com.dtflys.redistart.model.command.RSLuaRecord;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.command.RSCommandRecord;
import com.dtflys.redistart.service.CommandService;
import javafx.application.Platform;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.client.protocol.decoder.ListScanResult;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RSKeySet {
    private ObjectPropertyBase<RSKeyFindStatus> staus = new SimpleObjectProperty<>();
    private final RedisConnection connection;
    private final RedisDatabase database;
    private volatile long startIndex = 0;
    private volatile long endIndex = 0;
    private final Lock lock = new ReentrantLock();
    private ObservableList<RSKey> keyList = FXCollections.observableArrayList();
    private final CommandService commandService;

    public RSKeySet(RedisDatabase database, CommandService commandService) {
        this.database = database;
        connection = database.getConnection();
        this.commandService = commandService;
        setStaus(RSKeyFindStatus.INIT);
    }

    public void findNextPage() {
        int pageSize = connection.getConnectionConfig().getQueryPageSize();
        List<String> keys = new LinkedList<>();
        setStaus(RSKeyFindStatus.LOADING);
        new RSLuaRecord<>("findNextKeys.lua", RSKeyFindResult.class, endIndex, pageSize)
                .onResult(keyFindResult -> {
                    Platform.runLater(() -> {
                        lock.lock();
                        try {
                            keyList.addAll(keyFindResult.getKeys());
                            setStaus(RSKeyFindStatus.COMPLETED);
                        } finally {
                            lock.unlock();
                        }
                    });
                })
                .eval(connection, commandService);



/*
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
        */
    }

    public ObservableList<RSKey> getKeyList() {
        return keyList;
    }

    public void clear() {
        keyList.clear();
        startIndex = 0;
        endIndex = 0;
    }

    public RSKeyFindStatus getStaus() {
        return staus.get();
    }

    public ObjectPropertyBase<RSKeyFindStatus> stausProperty() {
        return staus;
    }

    public void setStaus(RSKeyFindStatus staus) {
        this.staus.set(staus);
    }
}
