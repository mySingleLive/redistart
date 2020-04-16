package com.dtflys.redistart.model.key;

public class RSLoadMore extends RSKey {

    private final RSKeySet keySet;

    public RSLoadMore(RSKeySet keySet) {
        this.keySet = keySet;
    }

    @Override
    public RSKeyType getType() {
        return null;
    }

    @Override
    public String getKey() {
        return "Load More";
    }

    public RSKeySet getKeySet() {
        return keySet;
    }
}
