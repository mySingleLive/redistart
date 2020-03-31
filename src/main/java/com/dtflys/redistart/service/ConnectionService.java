package com.dtflys.redistart.service;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionConfig;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component("connectionService")
public class ConnectionService {

    @Resource
    private RediStartService rediStartService;

    private ObservableList<RedisConnection> connections = FXCollections.observableArrayList();

    private ObservableList<RedisConnection> openedConnections = FXCollections.observableArrayList();

    private ObjectProperty<RedisConnection> selectedConnection = new SimpleObjectProperty<>();

    private Map<RedisConnectionConfig, RedisConnection> connectionConfigMap = new HashMap<>();

    private Consumer<RedisConnection> onAfterAddConnection;

    private Consumer<RedisConnection> onSelectOpenedConnection;

    private Consumer<RedisConnection> onAfterSelectConnection;

    public RedisConnection addConnection(RedisConnectionConfig connectionConfig) {
        RedisConnection connection = new RedisConnection(this, connectionConfig);
        connections.add(connection);
        connectionConfigMap.put(connectionConfig, connection);
        afterAddConnection(connection);
        return connection;
    }

    public void deleteConnection(RedisConnection connection) {
        connectionConfigMap.remove(connection.getConnectionConfig());
        connections.remove(connection);
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
        RedisConnection connection = new RedisConnection(this, connectionConfig);
        new Thread(() -> {
            Boolean result = connection.testConnect();
            if (onTestEnd != null) {
                onTestEnd.accept(result);
            }
        }).start();
    }

    public boolean isOpened(RedisConnection connection) {
        return openedConnections.contains(connection);
    }

    public ObservableList<RedisConnection> getConnections() {
        return connections;
    }

    public ObservableList<RedisConnection> getOpenedConnections() {
        return openedConnections;
    }

    public RedisConnection getSelectedConnection() {
        return selectedConnection.get();
    }

    public ObjectProperty<RedisConnection> selectedConnectionProperty() {
        return selectedConnection;
    }

    public void setSelectedConnection(RedisConnection selectedConnection) {
        this.selectedConnection.set(selectedConnection);
        rediStartService.setSelectedKeysContentPage(selectedConnection);
    }
}
