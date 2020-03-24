package com.dtflys.redistart.service;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionConfig;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component("connectionService")
public class ConnectionService {

    private List<RedisConnection> connections = new ArrayList<>();

    private Map<RedisConnectionConfig, RedisConnection> connectionConfigMap = new HashMap<>();

    private Consumer<RedisConnection> onAfterAddConnection;

    public RedisConnection addConnection(RedisConnectionConfig connectionConfig) {
        RedisConnection connection = new RedisConnection(connectionConfig);
        connections.add(connection);
        connectionConfigMap.put(connectionConfig, connection);
        afterAddConnection(connection);
        return connection;
    }

    public void setOnAfterAddConnection(Consumer<RedisConnection> onAfterAddConnection) {
        this.onAfterAddConnection = onAfterAddConnection;
    }

    private void afterAddConnection(RedisConnection connection) {
        if (onAfterAddConnection != null) {
            onAfterAddConnection.accept(connection);
        }
    }


    public void startTestConnection(RedisConnectionConfig connectionConfig, Consumer<Boolean> onTestEnd) {
        RedisConnection connection = new RedisConnection(connectionConfig);
        new Thread(() -> {
            Boolean result = connection.testConnect();
            if (onTestEnd != null) {
                onTestEnd.accept(result);
            }
        }).start();
    }

}