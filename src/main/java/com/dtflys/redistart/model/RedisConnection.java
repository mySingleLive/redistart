package com.dtflys.redistart.model;

import com.dtflys.redistart.event.RSEventHandlerList;
import org.apache.commons.collections4.MapUtils;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommands;

import java.util.*;

public class RedisConnection {

    private RedisConnectionConfig connectionConfig;

    private RedisClient redisClient;

    private org.redisson.client.RedisConnection redisConnection;

    private List<RedisDatabase> databaseList = new ArrayList<>();

    private RSEventHandlerList<RedisConnection> onBeforeOpenConnection = new RSEventHandlerList<>();

    private RSEventHandlerList<RedisConnection> onAfterOpenConnection = new RSEventHandlerList<>();

    private RSEventHandlerList<RedisDatabase> onBeforeOpenDatabase = new RSEventHandlerList<>();

    private RSEventHandlerList<RedisDatabase> onAfterOpenDatabase = new RSEventHandlerList<>();

    private RedisConnectionStatus status;

    public RedisConnection(RedisConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
        this.status = RedisConnectionStatus.CLOSED;
    }

    public RedisConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }


    private RedisClientConfig createRedisConnectionConfig() {
        RedisClientConfig config = new RedisClientConfig();
        config.setAddress(connectionConfig.getRedisHost(), connectionConfig.getRedisPort())
        .setClientName(connectionConfig.getName())
        .setPassword(connectionConfig.getRedisPassword())
        .setDatabase(0);
        return config;
    }

    public void openConnection() {
        status = RedisConnectionStatus.CONNECTING;
        onBeforeOpenConnection.handle(this);
        new Thread(() -> {
            boolean success = false;
            try {
                doOpenConnection();
                success = true;
            } catch (Throwable th) {
                closeConnection();
            }
            if (!success) {
                return;
            }
            databaseList = loadDatabases();
            status = RedisConnectionStatus.OPENED;
            onAfterOpenConnection.handle(this);
        }).start();
    }

    private void doOpenConnection() {
        RedisClientConfig config = createRedisConnectionConfig();
        redisClient = RedisClient.create(config);
        redisConnection = redisClient.connect();
    }

    private Map<String, Object> parseInfoKeysapceResult(String db) {
        String[] dbProps = db.split(",");
        Map<String, Object> results = new HashMap<>();
        for (String dbProp : dbProps) {
            String[] keyValue = dbProp.split("=");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            switch (key) {
                case "keys":
                case "avg_ttl":
                    results.put(key, Long.parseLong(value));
                    break;
                case "expires":
                    results.put(key, Integer.parseInt(value));
                    break;
                default:
                    break;
            }
        }
        return results;
    }

    private List<RedisDatabase> loadDatabases() {
        Map<String, String> dbMap = redisConnection.sync(StringCodec.INSTANCE, RedisCommands.INFO_KEYSPACE);
        List<String> dbNameList = new ArrayList<>(dbMap.keySet());
        var databaseList = new ArrayList<RedisDatabase>();
        for (String dbName : dbNameList) {
            RedisDatabase database = new RedisDatabase(this);
            String db = MapUtils.getString(dbMap, dbName);
            Map<String, Object> dbValues = parseInfoKeysapceResult(db);
            Integer index = Integer.parseInt(dbName.substring(2));
            Long keys = MapUtils.getLong(dbValues, "keys");
            Integer expires = MapUtils.getInteger(dbValues, "expires");
            Long averageTTL = MapUtils.getLong(dbValues, "avg_ttl");
            database.setName(dbName);
            database.setIndex(index);
            database.setSize(keys);
            database.setExpires(expires);
            database.setAverageTTL(averageTTL);
            databaseList.add(database);
        }
        databaseList.sort(Comparator.comparing(RedisDatabase::getIndex));
        return databaseList;
    }

    public boolean testConnect() {
        try {
            doOpenConnection();
            return true;
        } catch (Throwable th) {
            return false;
        } finally {
            closeConnection();
        }
    }

    public String ping() {
        String response = redisConnection.sync(new StringCodec(), RedisCommands.PING);
        return response;
    }

    public void closeConnection() {
        if (redisClient != null) {
            try {
                redisClient.shutdown();
            } catch (Throwable th) {
            }
        }
    }

    public List<RedisDatabase> getDatabaseList() {
        return databaseList;
    }

    public RSEventHandlerList<RedisConnection> getOnBeforeOpenConnection() {
        return onBeforeOpenConnection;
    }

    public RSEventHandlerList<RedisConnection> getOnAfterOpenConnection() {
        return onAfterOpenConnection;
    }

    public RSEventHandlerList<RedisDatabase> getOnBeforeOpenDatabase() {
        return onBeforeOpenDatabase;
    }

    public RSEventHandlerList<RedisDatabase> getOnAfterOpenDatabase() {
        return onAfterOpenDatabase;
    }

    public RedisConnectionStatus getStatus() {
        return status;
    }

    public boolean isClosed() {
        return status == RedisConnectionStatus.CLOSED;
    }

    public boolean isConnecting() {
        return status == RedisConnectionStatus.CONNECTING;
    }
}
