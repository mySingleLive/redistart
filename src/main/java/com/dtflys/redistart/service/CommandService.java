package com.dtflys.redistart.service;

import com.dtflys.redistart.model.command.RSCommandRecord;
import com.dtflys.redistart.model.command.RSLuaRecord;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.script.RedisNamedScript;
import com.dtflys.redistart.script.RedisScriptManager;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Component
public class CommandService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final RedisScriptManager redisScriptManager = RedisScriptManager.scriptManager();

    public void execute(RedisConnection connection, RSCommandRecord commandRecord) {
        executorService.submit(() -> {
            connection.sync(commandRecord);
        });
    }

    public <T> void evalLuaScript(RedisConnection connection, RSLuaRecord<T> luaRecord) {
        RedisNamedScript redisNamedScript = redisScriptManager.getNamedScript(luaRecord.getScriptName());
        if (redisNamedScript != null) {
            Class<T> resultType = luaRecord.getResultType();
            executorService.submit(() -> {
                T result = redisNamedScript.eval(connection, resultType, luaRecord.getArguments());
                luaRecord.doOnResult(result);
            });
        }
    }

}
