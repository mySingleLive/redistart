package com.dtflys.redistart.model.connection;

import com.dtflys.redistart.event.RSEventHandlerList;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.model.command.RSCommandRecord;
import org.redisson.api.RBatch;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BasicRedisConnection extends Conntable {

    protected RedisClient redisClient;

    protected org.redisson.client.RedisConnection redisConnection;

    private RSEventHandlerList<BasicRedisConnection> onOpenConnectionFailed = new RSEventHandlerList<>();

    private ExecutorService executorService = Executors.newFixedThreadPool(50);

    protected BasicRedisConnection(RedisConnectionConfig connectionConfig) {
        super(connectionConfig);
    }

    protected RedisClientConfig createRedisConnectionConfig(int dbIndex) {
        RedisClientConfig config = new RedisClientConfig();
        config.setAddress(connectionConfig.getRedisHost(), connectionConfig.getRedisPort())
                .setClientName(redisClientName(dbIndex))
                .setPassword(connectionConfig.getRedisPassword())
                .setDatabase(dbIndex);
        return config;
    }

    @Override
    protected void doOpenConnection(int dbIndex) {
        RedisClientConfig config = createRedisConnectionConfig(dbIndex);
        redisClient = RedisClient.create(config);
        redisConnection = redisClient.connect();
        RBatch s;
    }

    @Override
    public void closeConnection() {
        if (redisClient != null) {
            try {
                redisClient.shutdown();
            } catch (Throwable th) {
            } finally {
                afterCloseConnection();
            }
        }
    }


    public <T> T sync(RSCommandRecord record) {
        T ret = (T) redisConnection.sync(record.getRedisCommand(), record.getArguments());
        record.doOnResult(ret);
        return ret;
    }


    public void execute(RSCommandRecord record) {
        executorService.submit(() -> {
            sync(record);
        });
    }

    public RSEventHandlerList<BasicRedisConnection> getOnOpenConnectionFailed() {
        return onOpenConnectionFailed;
    }


    protected String redisClientName(int dbIndex) {
        return connectionConfig.getRedisHost() + ":" + connectionConfig.getRedisPort() + ":" + dbIndex;
    }


    protected abstract void afterCloseConnection();

}
