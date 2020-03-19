package com.dtflys.redistart.model;

import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommand;
import org.redisson.client.protocol.RedisCommands;

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


    private RedisClientConfig createRedisConnectionConfig() {
        RedisClientConfig config = new RedisClientConfig();
        config.setAddress(connectionConfig.getRedisHost(), connectionConfig.getRedisPort())
        .setClientName(connectionConfig.getName())
        .setPassword(connectionConfig.getRedisPassword())
        .setDatabase(0);
        return config;
    }

    public void openConnection() {
        RedisClientConfig config = createRedisConnectionConfig();
        redisClient = RedisClient.create(config);
        redisConnection = redisClient.connect();
    }

    public String ping() {
        String response = redisConnection.sync(new StringCodec(), RedisCommands.PING);
        return response;
    }

    public void closeConnection() {
        redisClient.shutdown();
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
