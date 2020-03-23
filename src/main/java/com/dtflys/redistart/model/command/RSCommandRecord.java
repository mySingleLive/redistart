package com.dtflys.redistart.model.command;

import org.redisson.client.protocol.RedisCommand;

public class RSCommandRecord {

    private final RedisCommand redisCommand;

    private final Object[] arguments;

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
}
