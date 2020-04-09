package com.dtflys.redistart.model.connection;

import com.dtflys.redistart.event.RSEventHandlerList;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.model.RedisConnectionStatus;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.service.CommandService;
import com.dtflys.redistart.service.ConnectionService;
import com.google.common.collect.Lists;
import javafx.beans.property.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommand;
import org.redisson.client.protocol.RedisCommands;

import java.util.*;

public class RedisConnection extends BasicRedisConnection {

    private final ConnectionService connectionService;

    private ObjectPropertyBase<RedisDatabase> selectedDatabase = new SimpleObjectProperty<>();

    private List<RedisDatabase> databaseList = new ArrayList<>();

    private RSEventHandlerList<RedisConnection> onBeforeOpenConnection = new RSEventHandlerList<>();

    private RSEventHandlerList<RedisConnection> onAfterOpenConnection = new RSEventHandlerList<>();

    private RSEventHandlerList<RedisDatabase> onBeforeOpenDatabase = new RSEventHandlerList<>();

    private RSEventHandlerList<RedisDatabase> onAfterOpenDatabase = new RSEventHandlerList<>();

    private ObjectPropertyBase<RedisConnectionStatus> status = new SimpleObjectProperty<>();

    public RedisConnection(ConnectionService connectionService, RedisConnectionConfig connectionConfig, CommandService commandService) {
        super(connectionConfig, commandService);
        this.connectionService = connectionService;
        setStatus(RedisConnectionStatus.CLOSED);

        selectedDatabase.addListener((observableValue, oldDatabase, newDatabase) -> {
            selectDatabase(newDatabase.getIndex());
            newDatabase.openDatabase();
        });
    }

    public void openConnection() {
        onBeforeOpenConnection.handle(this);
        new Thread(() -> {
            boolean hasOpened = connectionService.getOpenedConnections().contains(this);
            if (!hasOpened) {
                boolean success = false;
                try {
                    setStatus(RedisConnectionStatus.CONNECTING);
                    doOpenConnection(0);
                    success = true;
                } catch (Throwable th) {
                    closeConnection();
                }
                if (!success) {
                    getOnOpenConnectionFailed().handle(this);
                    return;
                }
                databaseList = loadDatabases();
                setStatus(RedisConnectionStatus.OPENED);
                connectionService.getOpenedConnections().add(this);
            }
            onAfterOpenConnection.handle(this);
            connectionService.afterOpenConnection(this);
            connectionService.setSelectedConnection(this);
        }).start();
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
            RedisDatabase database = new RedisDatabase(this, commandService);
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
        if (!databaseList.isEmpty() && getSelectedDatabase() == null) {
            // Select first database as default
            setSelectedDatabase(databaseList.get(0));
        }
        return databaseList;
    }

    private RedisCommand getEvalCommandByClass(Class resultType) {
        if (void.class.isAssignableFrom(resultType)) {
            return RedisCommands.EVAL_VOID;
        }
        if (CharSequence.class.isAssignableFrom(resultType)) {
            return RedisCommands.EVAL_STRING_DATA;
        }
        if (Boolean.class.isAssignableFrom(resultType) ||
                boolean.class.isAssignableFrom(resultType)) {
            return RedisCommands.EVAL_BOOLEAN;
        }
        if (Integer.class.isAssignableFrom(resultType) ||
                int.class.isAssignableFrom(resultType)) {
            return RedisCommands.EVAL_INTEGER;
        }
        if (Long.class.isAssignableFrom(resultType) ||
                long.class.isAssignableFrom(resultType)) {
            return RedisCommands.EVAL_LONG;
        }
        if (Double.class.isAssignableFrom(resultType) ||
                double.class.isAssignableFrom(resultType) ||
                Float.class.isAssignableFrom(resultType) ||
                float.class.isAssignableFrom(resultType)) {
            return RedisCommands.EVAL_DOUBLE;
        }
        if (List.class.isAssignableFrom(resultType)) {
            return RedisCommands.EVAL_LIST;
        }
        if (Map.class.isAssignableFrom(resultType)) {
            return RedisCommands.EVAL_MAP;
        }
        if (Set.class.isAssignableFrom(resultType)) {
            return RedisCommands.EVAL_SET;
        }
        return RedisCommands.EVAL_OBJECT;
    }

    public <T> T eval(String script, Class<T> resultType, String keys[], Object values[]) {
        RedisCommand evalCommand = getEvalCommandByClass(resultType);
        int keyLength = 0;
        if (keys != null) {
            keyLength = keys.length;
        }
        List<Object> argList = Lists.newArrayList(script, keyLength);
        for (String key: keys) {
            argList.add(key);
        }
        if (ArrayUtils.isNotEmpty(values)) {
            for (Object val : values) {
                argList.add(val);
            }
        }
        T result = (T) redisConnection.sync(evalCommand, argList.toArray());
        return result;
    }

    public boolean testConnect() {
        try {
            doOpenConnection(0);
            return true;
        } catch (Throwable th) {
            return false;
        } finally {
            closeConnection();
        }
    }

/*
    public String ping() {
        String response = redisConnection.sync(new StringCodec(), RedisCommands.PING);
        return response;
    }
*/

    @Override
    protected void afterCloseConnection() {
        setStatus(RedisConnectionStatus.CLOSED);
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
        return status.get();
    }

    public ObjectPropertyBase<RedisConnectionStatus> statusProperty() {
        return status;
    }

    public void setStatus(RedisConnectionStatus status) {
        this.status.set(status);
    }

    public StringProperty nameProperty() {
        StringProperty nameProperty = new SimpleStringProperty(connectionConfig.getName());
        nameProperty.addListener((observableValue, s, t1) -> nameProperty.setValue(s));
        return nameProperty;
    }

    public RedisDatabase getSelectedDatabase() {
        return selectedDatabase.get();
    }

    public ObjectPropertyBase<RedisDatabase> selectedDatabaseProperty() {
        return selectedDatabase;
    }

    public void setSelectedDatabase(RedisDatabase selectedDatabase) {
        this.selectedDatabase.set(selectedDatabase);
    }

    @Override
    public String toString() {
        return connectionConfig.getName();
    }
}
