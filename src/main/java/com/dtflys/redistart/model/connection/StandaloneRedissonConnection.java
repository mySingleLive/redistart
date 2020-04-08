package com.dtflys.redistart.model.connection;

import com.dtflys.redistart.model.RedisConnectionConfig;
import org.redisson.Redisson;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

public class StandaloneRedissonConnection extends Conntable {

    private Config config;

    private RedissonClient redisson;

    protected StandaloneRedissonConnection(RedisConnectionConfig connectionConfig) {
        super(connectionConfig);
    }

    public Config createConfig() {
        Config config = new Config();
        config.setCodec(new StringCodec());
        String address = "redis://" + connectionConfig.getRedisHost() + ":" + connectionConfig.getRedisPort();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(address);
        singleServerConfig.setConnectionPoolSize(100);
        singleServerConfig.setConnectTimeout(connectionConfig.getConnectTimeout());
        singleServerConfig.setTimeout(connectionConfig.getTimeout());
        this.config = config;
        return config;
    }

    public void sync() {
        RBatch batch = redisson.createBatch();

    }

    @Override
    protected void doOpenConnection(int dbIndex) {
        redisson = Redisson.create(createConfig());
    }

    @Override
    public void closeConnection() {
        redisson.shutdown();
    }
}
