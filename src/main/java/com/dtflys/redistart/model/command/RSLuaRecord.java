package com.dtflys.redistart.model.command;

import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.service.CommandService;
import org.redisson.client.protocol.RedisCommand;

import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;

public class RSLuaRecord<T> extends RSCommandRecord<T> {
    private final String scriptName;
    private String[] keys;
    private final Class<T> resultType;

    public RSLuaRecord(String scriptName, Class<T> resultType, Object... args) {
        super(null, args);
        this.scriptName = scriptName;
        this.resultType = resultType;
    }

    public String getScriptName() {
        return scriptName;
    }

    public RSLuaRecord<T> keys(String ...keys) {
        this.keys = keys;
        return this;
    }

    public String[] getKeys() {
        return keys;
    }

    public Class<T> getResultType() {
        return resultType;
    }

    @Override
    public RSLuaRecord<T> onResult(Consumer<T> onResult) {
        return (RSLuaRecord<T>) super.onResult(onResult);
    }

    public void eval(RedisConnection connection, CommandService commandService) {
        commandService.evalLuaScript(connection, this);
    }
}
