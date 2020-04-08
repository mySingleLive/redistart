package com.dtflys.redistart.model.command;

import org.redisson.client.protocol.RedisCommand;
import org.redisson.client.protocol.decoder.ListScanResult;

import java.util.function.Consumer;

public class RSCommandRecord<T> {

    private final RedisCommand redisCommand;

    private final Object[] arguments;

    private Consumer<T> onResult;

    public RSCommandRecord(RedisCommand redisCommand, Object ...args) {
        this.redisCommand = redisCommand;
        this.arguments = args;
    }

    public RedisCommand getRedisCommand() {
        return redisCommand;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public RSCommandRecord<T> onResult(Consumer<T> onResult) {
        this.onResult = onResult;
        return this;
    }

    public void doOnResult(T obj) {
        if (onResult != null) {
            onResult.accept(obj);
        }
    }

}
