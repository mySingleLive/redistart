package com.dtflys.redistart.model.key;

import com.dtflys.redistart.model.command.RSLuaRecord;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.service.CommandService;
import javafx.application.Platform;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class RSKeySet {
    private ObjectPropertyBase<RSKeyFindStatus> status = new SimpleObjectProperty<>();
    private final RedisConnection connection;
    private final RedisDatabase database;
    private volatile long startIndex = 0;
    private volatile long endIndex = 0;
    private final Lock lock = new ReentrantLock();
    private ObservableList<RSKey> keyList = FXCollections.observableArrayList();
    private final CommandService commandService;
    private Consumer<RSKeyFindResult> onBeforeAddResults;
    private Consumer<RSKeyFindResult> onLoadCompleted;

    public RSKeySet(RedisDatabase database, CommandService commandService) {
        this.database = database;
        connection = database.getConnection();
        this.commandService = commandService;
        setStatus(RSKeyFindStatus.INIT);
    }

    public void findNextPage() {
        int pageSize = connection.getConnectionConfig().getQueryPageSize();
        List<String> keys = new LinkedList<>();
        setStatus(RSKeyFindStatus.LOADING);
        new RSLuaRecord<>("findNextKeys.lua", RSKeyFindResult.class, endIndex, pageSize)
                .onResult(keyFindResult -> {
                    Platform.runLater(() -> {
                        lock.lock();
                        try {
                            if (onBeforeAddResults != null) {
                                onBeforeAddResults.accept(keyFindResult);
                            }
                            keyList.addAll(keyFindResult.getKeys());
                            setStatus(RSKeyFindStatus.COMPLETED);
                            if (onLoadCompleted != null) {
                                onLoadCompleted.accept(keyFindResult);
                            }
                            endIndex = keyFindResult.getPos();
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


    public void setOnBeforeAddResults(Consumer<RSKeyFindResult> onBeforeAddResults) {
        this.onBeforeAddResults = onBeforeAddResults;
    }

    public void setOnLoadCompleted(Consumer<RSKeyFindResult> onLoadCompleted) {
        this.onLoadCompleted = onLoadCompleted;
    }

    public ObservableList<RSKey> getKeyList() {
        return keyList;
    }

    public void clear() {
        Platform.runLater(() -> {
            keyList.clear();
            startIndex = 0;
            endIndex = 0;
        });
    }

    public RSKeyFindStatus getStatus() {
        return status.get();
    }

    public ObjectPropertyBase<RSKeyFindStatus> statusProperty() {
        return status;
    }

    public void setStatus(RSKeyFindStatus status) {
        this.status.set(status);
    }
}
