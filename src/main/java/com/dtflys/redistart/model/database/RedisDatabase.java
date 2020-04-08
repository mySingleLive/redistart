package com.dtflys.redistart.model.database;

import com.dtflys.redistart.model.connection.BasicRedisConnection;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.key.RSKeySet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.redisson.client.RedisClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RedisDatabase extends BasicRedisConnection {

    private final RedisConnection connection;

    private RedisClient redisClient;

    private org.redisson.client.RedisConnection redisConnection;

    private BooleanProperty opened = new SimpleBooleanProperty(false);

    private String name;

    private Integer index;

    private Long size;

    private Integer expires;

    private Long averageTTL;

    private RSKeySet keySet;

    public RedisDatabase(RedisConnection connection) {
        super(connection.getConnectionConfig());
        this.connection = connection;
        keySet = new RSKeySet(this);
    }

    public void openDatabase() {
        connection.getOnBeforeOpenDatabase().handle(this);
        new Thread(() -> {
            if (!isOpened()) {
                boolean success = false;
                try {
                    doOpenConnection(index);
                    success = true;
                } catch (Throwable th) {
                    closeConnection();
                }
                if (!success) {
                    getOnOpenConnectionFailed().handle(this);
                    return;
                }
                System.out.println("------ Open Database Completed ------");
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

    @Override
    protected void afterCloseConnection() {
        setOpened(false);
    }
}