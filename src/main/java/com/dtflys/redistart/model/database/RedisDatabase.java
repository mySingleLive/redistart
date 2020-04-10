package com.dtflys.redistart.model.database;

import com.dtflys.redistart.model.connection.BasicRedisConnection;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.key.RSKeySet;
import com.dtflys.redistart.service.CommandService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RedisDatabase {

    private final RedisConnection connection;

    private BooleanProperty opened = new SimpleBooleanProperty(false);

    private String name;

    private Integer index;

    private Long size;

    private Integer expires;

    private Long averageTTL;

    private RSKeySet keySet;

    public RedisDatabase(RedisConnection connection, CommandService commandService) {
//        super(connection.getConnectionConfig(), commandService);
        this.connection = connection;
        keySet = new RSKeySet(this, commandService);
    }

    public void openDatabase() {
        connection.getOnBeforeOpenDatabase().handle(this);
        new Thread(() -> {
            if (!isOpened()) {
//                connection.selectDatabase(index);
                System.out.println("------ Open Database Completed ------");
                keySet.clear();
                keySet.findNextPage();
                connection.getOnAfterOpenDatabase().handle(this);
            }
        }).start();
    }

    public void clear() {
        keySet.clear();
    }

    public RedisConnection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(" (");
        if (size / 1000 >= 1) {
            BigDecimal newSize = null;
            String unit;
            if (size / (1000 * 1000) >= 1) {
                newSize = new BigDecimal(size).divide(new BigDecimal(1000 * 1000), RoundingMode.HALF_UP);
                unit = "M";
            } else {
                newSize = new BigDecimal(size).divide(new BigDecimal(1000), RoundingMode.HALF_UP);
                unit = "K";
            }
            builder.append(newSize.toString() + unit);
        } else {
            builder.append(size);
        }
        builder.append(")");
        return builder.toString();
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

    public RSKeySet getKeySet() {
        return keySet;
    }

    public boolean isOpened() {
        return opened.get();
    }

    public BooleanProperty openedProperty() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened.set(opened);
    }

    public Long getAverageTTL() {
        return averageTTL;
    }

    public void setAverageTTL(Long averageTTL) {
        this.averageTTL = averageTTL;
    }

//    @Override
//    protected void afterCloseConnection() {
//        setOpened(false);
//    }
}
