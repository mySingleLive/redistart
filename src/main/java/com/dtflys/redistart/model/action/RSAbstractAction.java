package com.dtflys.redistart.model.action;


public abstract class RSAbstractAction implements RSAction {

    private final String actionName;

    public RSAbstractAction(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

}
