package com.dtflys.redistart.model.key;

import com.dtflys.redistart.model.command.RSLuaRecord;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.value.AbstractKeyValue;
import com.dtflys.redistart.model.lua.RSValueGetResult;
import com.dtflys.redistart.model.value.ValueBuilder;
import com.dtflys.redistart.service.CommandService;
import javafx.application.Platform;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;

import java.util.function.Consumer;

public class RSKey {

    private RedisDatabase database;

    private String key;

    private RSKeyType type;

    private Long ttl;

    private ObjectPropertyBase<AbstractKeyValue> value = new SimpleObjectProperty<>();

    private Consumer<AbstractKeyValue> onValueChange;

    public RSKey() {
        value.addListener((observableValue, oldVal, newValue) -> {
            if (onValueChange != null) {
                onValueChange.accept(newValue);
            }
        });
    }

    public RedisDatabase getDatabase() {
        return database;
    }

    public void setDatabase(RedisDatabase database) {
        this.database = database;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public RSKeyType getType() {
        return type;
    }

    public void setType(RSKeyType type) {
        this.type = type;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public AbstractKeyValue getValue() {
        return value.get();
    }

    public void setValue(AbstractKeyValue value) {
        this.value.set(value);
    }

    public Consumer<AbstractKeyValue> getOnValueChange() {
        return onValueChange;
    }

    public void setOnValueChange(Consumer<AbstractKeyValue> onValueChange) {
        this.onValueChange = onValueChange;
    }

    @Override
    public String toString() {
        return getKey();
    }

    public void get(CommandService commandService) {
        new RSLuaRecord<>("getValue.lua", RSValueGetResult.class, key, 0, 100)
                .onResult(valueGetResult -> {
                    Platform.runLater(() -> {
                        System.out.println(valueGetResult);
                        if (valueGetResult.getStatus() == 0) {
                            setType(valueGetResult.getType());
                            setTtl(valueGetResult.getTtl());
                            ValueBuilder builder = new ValueBuilder()
                                    .key(key)
                                    .type(type)
                                    .ttl(ttl)
                                    .value(valueGetResult.getValue());
                            AbstractKeyValue value = builder.build();
                            setValue(value);
                        }
                    });
                })
                .eval(database.getConnection(), commandService);
    }
}
