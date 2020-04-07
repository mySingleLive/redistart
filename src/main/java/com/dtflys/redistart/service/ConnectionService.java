package com.dtflys.redistart.service;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.storage.RedisConnectionConfigStorage;
import com.google.common.collect.Lists;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component("connectionService")
public class ConnectionService {

    @Resource
    private RediStartService rediStartService;

    @Resource
    private RedisConnectionConfigStorage redisConnectionConfigStorage;

    private ObservableList<RedisConnection> connections = FXCollections.observableArrayList();

    private ObservableList<RedisConnection> openedConnections = FXCollections.observableArrayList();

    private ObjectProperty<RedisConnection> selectedConnection = new SimpleObjectProperty<>();

    private Map<RedisConnectionConfig, RedisConnection> connectionConfigMap = new HashMap<>();

    private Consumer<RedisConnection> onAfterAddConnection;

    private Consumer<RedisConnection> onSelectOpenedConnection;

    private Consumer<RedisConnection> onAfterSelectConnection;

    public void loadConnections() {
        List<RedisConnectionConfig> connectionConfigs = redisConnectionConfigStorage.loadConnectionConfigList();
        connections.clear();
        if (CollectionUtils.isNotEmpty(connectionConfigs)) {
            for (RedisConnectionConfig connectionConfig : connectionConfigs) {
                createConnection(connectionConfig);
            }
        }
    }

    public RedisConnection addConnection(RedisConnectionConfig connectionConfig) {
        RedisConnection connection = createConnection(connectionConfig);
        redisConnectionConfigStorage.insertConnectionConfigList(Lists.newArrayList(connectionConfig));
        return connection;
    }

    private RedisConnection createConnection(RedisConnectionConfig connectionConfig) {
        RedisConnection connection = new RedisConnection(this, connectionConfig);
        connections.add(connection);
        connectionConfigMap.put(connectionConfig, connection);
        afterAddConnection(connection);
        return connection;
    }

    public void deleteConnection(RedisConnection connection) {
        connectionConfigMap.remove(connection.getConnectionConfig());
        connections.remove(connection);
        redisConnectionConfigStorage.deleteConnectionConfigList(Lists.newArrayList(connection.getConnectionConfig()));
    }

    public void updateAllConnections() {
        redisConnectionConfigStorage.updateAllConnectionConfigList(
                connections.stream().map(conn -> conn.getConnectionConfig()).collect(Collectors.toList()));
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

    public RedisConnection findConnectionByName(String name) {
        for (RedisConnection connection : connections) {
            if (connection.getConnectionConfig().getName().equals(name)) {
                return connection;
            }
        }
        return null;
    }

    public String getFixedConnectionName(String name, boolean modify) {
        do {
            RedisConnection connection = findConnectionByName(name);
            if (connection == null) {
                return name;
            }
            if (modify) {
                return connection.getConnectionConfig().getName();
            }
            String patternStr = "(?<NAME> (.)+)\\((?<NUMBER> [\\d]+)\\)";
            Pattern pattern = Pattern.compile(patternStr, Pattern.COMMENTS);
            Matcher matcher = pattern.matcher(name);
            String nameGroup = null;
            Integer num =  null;
            if (matcher.find()) {
                nameGroup = matcher.group("NAME");
                String numberGroup = matcher.group("NUMBER");
                try {
                    num = Integer.parseInt(numberGroup);
                } catch (Throwable th) {}
                if (num != null) {
                    num++;
                }
                name = nameGroup + "(" + num + ")";
            } else {
                name = name + "(1)";
            }
        } while (true);
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
