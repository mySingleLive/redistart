package com.dtflys.redistart.model.action;

public interface RSAction {

    String SELECT_KEY = "select-key";
    String SELECT_KEY_ON_NEW_TAB = "select-key-on-new-tab";

    String getActionName();

    String getActionDisplayName();

    void doAction(Object ...args);
}
