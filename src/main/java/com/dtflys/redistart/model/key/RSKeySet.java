package com.dtflys.redistart.model.key;

import com.dtflys.redistart.model.RedisDatabase;

import java.util.LinkedHashMap;
import java.util.Map;

public class RSKeySet {
    private final RedisDatabase database;
    private int startIndex = 0;
    private int endIndex = 0;
    private final int pageSize;
    private Map<String, RSKey> keyMap = new LinkedHashMap<>();

    public RSKeySet(RedisDatabase database, int pageSize) {
        this.database = database;
        this.pageSize = pageSize;
    }

    public void findNextPage() {

    }

}
