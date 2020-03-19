package com.dtflys.redistart.model;

import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;

public class RedisConnection {

    private RedisConnectionConfig connectionConfig;

    private RedisClient redisClient;

    private org.redisson.client.RedisConnection redisConnection;

    private RedisConnectionStatus status;

    public RedisConnection(RedisConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public RedisConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    private String getRedisAddress() {
        String host = connectionConfig.getRedisHost();
        Integer port = connectionConfig.getRedisPort();
        return "redis://" + host + ":" + port;
    }

    private RedisClientConfig createRedisConnectionConfig() {
        RedisClientConfig config = new RedisClientConfig();
        config.setAddress(connectionConfig.getRedisHost(), connectionConfig.getRedisPort())
        .setClientName(connectionConfig.getName())
        .setPassword(connectionConfig.getRedisPassword())
        .setCommandTimeout(connectionConfig.getTimeout())
        .setConnectTimeout(connectionConfig.getConnectTimeout())
        .setDatabase(0);
        return config;
    }

    public void openConnection() {
        RedisClientConfig config = createRedisConnectionConfig();
        redisClient = RedisClient.create(config);
        redisConnection = redisClient.connect();
    }

    public boolean info() {
        return true;
    }

    public void closeConnection() {
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
