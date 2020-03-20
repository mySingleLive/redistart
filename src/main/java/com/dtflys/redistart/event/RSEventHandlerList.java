package com.dtflys.redistart.event;

import java.util.ArrayList;
import java.util.List;

public class RSEventHandlerList<T> implements RSEventHandler<T> {

    private List<RSEventHandler<T>> handlers = new ArrayList<>();

    public void addHandler(RSEventHandler<T> handler) {
        handlers.add(handler);
    }

    @Override
    public void handle(T obj) {
        for (RSEventHandler<T> handler : handlers) {
            handler.handle(obj);
        }
    }
}
