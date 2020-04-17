package com.dtflys.redistart.model.key;

import java.util.List;

public class RSKeyFindResult {

    private Long pos;

    private Boolean search;

    private List<RSKey> keys;

    public Long getPos() {
        return pos;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }

    public Boolean getSearch() {
        return search;
    }

    public void setSearch(Boolean search) {
        this.search = search;
    }

    public boolean hasMoreKeys() {
        if (pos == null) return false;
        return pos.compareTo(0L) > 0;
    }

    public List<RSKey> getKeys() {
        return keys;
    }

    public void setKeys(List<RSKey> keys) {
        this.keys = keys;
    }
}
