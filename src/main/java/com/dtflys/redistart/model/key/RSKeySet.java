package com.dtflys.redistart.model.key;

import com.alibaba.fastjson.JSON;
import com.dtflys.redistart.model.command.RSLuaRecord;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.search.RSSearchInfo;
import com.dtflys.redistart.service.CommandService;
import javafx.application.Platform;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class RSKeySet {
    private ObjectPropertyBase<RSKeyFindStatus> status = new SimpleObjectProperty<>();
    private final RedisConnection connection;
    private final RedisDatabase database;
    private volatile long startIndex = 0;
    private volatile long lastIndex = 0;
    private volatile boolean finished = false;
    private final Lock lock = new ReentrantLock();
    private RSSearchInfo searchInfo = new RSSearchInfo();
    private ObservableList<RSKey> keyList = FXCollections.observableArrayList();
    private final CommandService commandService;
    private Consumer<RSKeyFindResult> onBeforeAddResults;
    private Consumer<RSKeyFindResult> onLoadCompleted;
    private int maxScanCount = 200;

    public RSKeySet(RedisDatabase database, CommandService commandService) {
        this.database = database;
        connection = database.getConnection();
        this.commandService = commandService;
        setStatus(RSKeyFindStatus.INIT);
        searchInfo.patternProperty().addListener((observableValue, oldValue, newValue) -> {
            refreshData();
        });
    }

    public void refreshData() {
        Platform.runLater(() -> {
            lock.lock();
            startIndex = lastIndex = 0;
            keyList.clear();
            lock.unlock();
            findNextPage();
        });
    }

    public void findNextPage() {
        int pageSize = connection.getConnectionConfig().getQueryPageSize();
        finished = false;
        findNextPage(pageSize, pageSize, 0);
    }

    public void findNextPage(int pageSize, int onePageSize, int scanCount) {
        if (getSearchInfo().isSearchMode()) {
            setStatus(RSKeyFindStatus.SEARCHING);
        } else {
            setStatus(RSKeyFindStatus.LOADING);
        }
        String searchJson = JSON.toJSONString(searchInfo);
        new RSLuaRecord<>("findNextKeys.lua", RSKeyFindResult.class, lastIndex, pageSize, searchJson)
                .onResult(keyFindResult -> {
                    Platform.runLater(() -> {
                        if (finished) {
                            return;
                        }
                        lock.lock();
                        boolean autoSearchNextPage = false;
                        try {
                            if (onBeforeAddResults != null) {
                                onBeforeAddResults.accept(keyFindResult);
                            }
                            for (RSKey key : keyFindResult.getKeys()) {
                                if (key.getKey() != null) {
                                    keyList.add(key);
                                }
                            }
                            if (keyFindResult.getSearch()) {
                                if (keyList.size() < onePageSize - 5 && scanCount < maxScanCount) {
                                    setStatus(RSKeyFindStatus.SEARCHING);
                                    autoSearchNextPage = true;
                                } else {
                                    setStatus(RSKeyFindStatus.SEARCH_PAGE_COMPLETED);
                                }
                            } else {
                                setStatus(RSKeyFindStatus.LOAD_PAGE_COMPLETED);
                            }
                            if (onLoadCompleted != null) {
                                onLoadCompleted.accept(keyFindResult);
                            }
                            lastIndex = keyFindResult.getPos();
                            finished = !keyFindResult.hasMoreKeys();
                        } finally {
                            lock.unlock();
                            if (keyFindResult.hasMoreKeys() && autoSearchNextPage) {
                                int newPageSize = onePageSize - keyList.size();
                                findNextPage(newPageSize, onePageSize, scanCount + 1);
                            }
                        }
                    });
                })
                .eval(connection, commandService);
    }

    public RSSearchInfo getSearchInfo() {
        return searchInfo;
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
            lastIndex = 0;
        });
    }

    public long getLastIndex() {
        return lastIndex;
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
