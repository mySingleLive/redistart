package com.dtflys.redistart.model.key;

import java.util.List;

public class RSKeyFindResult {

    private Long pos;

    private List<RSKey> keys;

    public Long getPos() {
        return pos;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }

    public List<RSKey> getKeys() {
        return keys;
    }

    public void setKeys(List<RSKey> keys) {
        this.keys = keys;
    }
}
