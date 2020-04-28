package com.dtflys.redistart.model.key;

import com.alibaba.fastjson.JSON;
import com.dtflys.redistart.model.command.RSLuaRecord;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.lua.RSAddKeyResult;
import com.dtflys.redistart.model.lua.RSKeyFindResult;
import com.dtflys.redistart.model.search.RSSearchCondition;
import com.dtflys.redistart.model.ttl.RSTtlOperator;
import com.dtflys.redistart.service.CommandService;
import javafx.application.Platform;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.List;
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
    private RSSearchCondition searchCondition = new RSSearchCondition();
    private ObservableList<RSKey> keyList = FXCollections.observableArrayList();
    private List<RSKey> addedKeyList = new LinkedList<>();
    private final CommandService commandService;
    private Consumer<RSKeyFindResult> onBeforeAddResults;
    private Consumer<RSKeyFindResult> onLoadCompleted;
    private int maxScanCount = 200;

    public RSKeySet(RedisDatabase database, CommandService commandService) {
        this.database = database;
        connection = database.getConnection();
        this.commandService = commandService;
        setStatus(RSKeyFindStatus.INIT);
        searchCondition.patternProperty().addListener((observableValue, oldValue, newValue) -> {
            if (oldValue != null) {
                refreshData();
            }
        });
        searchCondition.typesProperty().addListener((observableValue, oldValue, newValue) -> {
            if (oldValue != null && (
                    getStatus() == RSKeyFindStatus.SEARCH_PAGE_COMPLETED
                    || getStatus() == RSKeyFindStatus.LOAD_PAGE_COMPLETED
            )) {
                refreshData();
            }
        });
        searchCondition.ttlOperatorProperty().addListener((observableValue, oldOp, newOp) -> {
            if (oldOp != null && (
                    getStatus() == RSKeyFindStatus.SEARCH_PAGE_COMPLETED
                            || getStatus() == RSKeyFindStatus.LOAD_PAGE_COMPLETED
            )) {
                refreshData();
            }
        });
        searchCondition.ttlProperty().addListener((observableValue, oldVal, newVal) -> {
            if (oldVal != null && (
                    getStatus() == RSKeyFindStatus.SEARCH_PAGE_COMPLETED
                            || getStatus() == RSKeyFindStatus.LOAD_PAGE_COMPLETED
            )) {
                refreshData();
            }
        });

    }

    public List<RSKey> getAddedKeyList() {
        return addedKeyList;
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
        if (getSearchCondition().isSearchMode()) {
            setStatus(RSKeyFindStatus.SEARCHING);
        } else {
            setStatus(RSKeyFindStatus.LOADING);
        }
        String searchJson = JSON.toJSONString(searchCondition);
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
                                    key.setDatabase(database);
                                    keyList.add(key);
                                }
                            }
                            lastIndex = keyFindResult.getPos();
                            finished = !keyFindResult.hasMoreKeys();

                            if (keyFindResult.getSearch()) {
                                if (!finished && keyList.size() < onePageSize - 5 && scanCount < maxScanCount) {
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
                        } finally {
                            lock.unlock();
                            if (!finished && autoSearchNextPage) {
                                int newPageSize = onePageSize - keyList.size();
                                findNextPage(newPageSize, onePageSize, scanCount + 1);
                            }
                        }
                    });
                })
                .eval(connection, commandService);
    }

    public void addNewKey(RSKeyType type, String keyName, Consumer<RSAddKeyResult> onAddKeyComplete) {
        switch (type) {
            case string:
                addNewStringKey(keyName, onAddKeyComplete);
                break;
            case hash:
                break;
        }
    }

    public void addNewStringKey(String keyName, Consumer<RSAddKeyResult> onAddKeyComplete) {
        new RSLuaRecord<>("addStringKey.lua", RSAddKeyResult.class, keyName, "")
                .onResult(result -> {
                    Platform.runLater(() -> {
                        if (onAddKeyComplete != null) {
                            addKeyToList(result);
                            onAddKeyComplete.accept(result);
                        }
                    });
                })
                .eval(connection, commandService);
    }

    private RSKey addKeyToList(RSAddKeyResult result) {
        RSKey newKey = new RSKey();
        newKey.setDatabase(database);
        newKey.setKey(result.getKeyName());
        newKey.setType(result.getType());
        newKey.setTtl(-1L);
        if (keyList.size() > 2 && keyList.get(keyList.size() - 1) instanceof RSLoadMore) {
            keyList.add(keyList.size() - 1, newKey);
        } else {
            keyList.add(newKey);
        }
        addedKeyList.add(newKey);
        result.setKey(newKey);
        return newKey;
    }

    public RSSearchCondition getSearchCondition() {
        return searchCondition;
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
            addedKeyList.clear();
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
