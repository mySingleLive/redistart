package com.dtflys.redistart.service;

import com.dtflys.redistart.model.action.RSAction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("actionService")
public class ActionService {

    private Map<String, RSAction> actionMap = new HashMap<>();

    public void registerAction(RSAction action) {
        actionMap.put(action.getActionName(), action);
    }

    public RSAction getAction(String actionName) {
        return actionMap.get(actionName);
    }

    public void doAction(String actionName, Object... args) {
        RSAction action = getAction(actionName);
        if (action == null) {
            throw new RuntimeException("Action \"" + actionName + "\" dose not exist");
        }
        action.doAction(args);
    }
}
