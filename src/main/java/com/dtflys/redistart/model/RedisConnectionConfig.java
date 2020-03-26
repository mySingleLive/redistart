package com.dtflys.redistart.model;

import javafx.beans.property.*;

import java.util.Date;

public class RedisConnectionConfig {

    /* Connection Settings */

    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty redisHost = new SimpleStringProperty();

    private final IntegerProperty redisPort = new SimpleIntegerProperty();

    private final StringProperty redisPassword = new SimpleStringProperty();

    /* SSL Settings */

    private final BooleanProperty isUseSSL = new SimpleBooleanProperty(false);

    /* SSH Settings */

    private final BooleanProperty isUseSSH = new SimpleBooleanProperty(false);

    /* Advanced Settings */

    private final IntegerProperty timeout = new SimpleIntegerProperty();

    private final IntegerProperty connectTimeout = new SimpleIntegerProperty();

    private final IntegerProperty reconnectTimeout = new SimpleIntegerProperty();

    private final IntegerProperty queryPageSize = new SimpleIntegerProperty();

    private final ObjectProperty<Date> createTime = new SimpleObjectProperty<>();

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getRedisHost() {
        return redisHost.get();
    }

    public StringProperty redisHostProperty() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost.set(redisHost);
    }

    public int getRedisPort() {
        return redisPort.get();
    }

    public IntegerProperty redisPortProperty() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort.set(redisPort);
    }

    public String getRedisPassword() {
        return redisPassword.get();
    }

    public StringProperty redisPasswordProperty() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword.set(redisPassword);
    }

    public boolean getIsUseSSL() {
        return isUseSSL.get();
    }

    public BooleanProperty isUseSSLProperty() {
        return isUseSSL;
    }

    public void setIsUseSSL(boolean isUseSSL) {
        this.isUseSSL.set(isUseSSL);
    }

    public boolean getIsUseSSH() {
        return isUseSSH.get();
    }

    public BooleanProperty isUseSSHProperty() {
        return isUseSSH;
    }

    public void setIsUseSSH(boolean isUseSSH) {
        this.isUseSSH.set(isUseSSH);
    }

    public int getTimeout() {
        return timeout.get();
    }

    public IntegerProperty timeoutProperty() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout.set(timeout);
    }

    public int getConnectTimeout() {
        return connectTimeout.get();
    }

    public IntegerProperty connectTimeoutProperty() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout.set(connectTimeout);
    }

    public int getReconnectTimeout() {
        return reconnectTimeout.get();
    }

    public IntegerProperty reconnectTimeoutProperty() {
        return reconnectTimeout;
    }

    public void setReconnectTimeout(int reconnectTimeout) {
        this.reconnectTimeout.set(reconnectTimeout);
    }

    public int getQueryPageSize() {
        return queryPageSize.get();
    }

    public IntegerProperty queryPageSizeProperty() {
        return queryPageSize;
    }

    public void setQueryPageSize(int queryPageSize) {
        this.queryPageSize.set(queryPageSize);
    }

    public Date getCreateTime() {
        return createTime.get();
    }

    public ObjectProperty<Date> createTimeProperty() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime.set(createTime);
    }
}
