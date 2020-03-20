package com.dtflys.redistart.model;

import com.dtflys.redistart.model.key.RSKeySet;

public class RedisDatabase {

    private final RedisConnection connection;

    private boolean isOpened = false;

    private String name;

    private Integer index;

    private Long size;

    private Integer expires;

    private Long averageTTL;

    private final RSKeySet keySet;

    public RedisDatabase(RedisConnection connection) {
        this.connection = connection;
        keySet = new RSKeySet(this, connection.getConnectionConfig().getQueryPageSize());
    }

    public void openDatabase() {
        if (!isOpened) {
            connection.getOnBeforeOpenDatabase().handle(this);

            connection.getOnAfterOpenDatabase().handle(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public Long getAverageTTL() {
        return averageTTL;
    }

    public void setAverageTTL(Long averageTTL) {
        this.averageTTL = averageTTL;
    }
}
