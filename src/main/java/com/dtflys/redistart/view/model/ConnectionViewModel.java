package com.dtflys.redistart.view.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class ConnectionViewModel {

    /* Connection Settings */

    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty redisHost = new SimpleStringProperty("localhost");

    private final IntegerProperty redisPort = new SimpleIntegerProperty(6379);

    private final StringProperty redisPassword = new SimpleStringProperty();

    private final BooleanProperty passwordVisible = new SimpleBooleanProperty(false);

    /* SSL Settings */

    private final BooleanProperty isUseSSL = new SimpleBooleanProperty(false);

    /* SSH Settings */

    private final BooleanProperty isUseSSH = new SimpleBooleanProperty(false);

    private final StringProperty sshHost = new SimpleStringProperty();

    private final IntegerProperty sshPort = new SimpleIntegerProperty(22);

    private final StringProperty sshUsername = new SimpleStringProperty();

    private final StringProperty sshAuthType = new SimpleStringProperty();

    private final StringProperty sshPrivateKeyFile = new SimpleStringProperty();

    private final StringProperty sshPass = new SimpleStringProperty();

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

    public StringProperty getNameProperty() {
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

    public boolean isPasswordVisible() {
        return passwordVisible.get();
    }

    public BooleanProperty passwordVisibleProperty() {
        return passwordVisible;
    }

    public void setPasswordVisible(boolean passwordVisible) {
        this.passwordVisible.set(passwordVisible);
    }

    public boolean isIsUseSSL() {
        return isUseSSL.get();
    }

    public BooleanProperty isUseSSLProperty() {
        return isUseSSL;
    }

    public void setIsUseSSL(boolean isUseSSL) {
        this.isUseSSL.set(isUseSSL);
    }

    public boolean isIsUseSSH() {
        return isUseSSH.get();
    }

    public BooleanProperty isUseSSHProperty() {
        return isUseSSH;
    }

    public void setIsUseSSH(boolean isUseSSH) {
        this.isUseSSH.set(isUseSSH);
    }

    public String getSshHost() {
        return sshHost.get();
    }

    public StringProperty sshHostProperty() {
        return sshHost;
    }

    public void setSshHost(String sshHost) {
        this.sshHost.set(sshHost);
    }

    public int getSshPort() {
        return sshPort.get();
    }

    public IntegerProperty sshPortProperty() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort.set(sshPort);
    }

    public String getSshUsername() {
        return sshUsername.get();
    }

    public StringProperty sshUsernameProperty() {
        return sshUsername;
    }

    public void setSshUsername(String sshUsername) {
        this.sshUsername.set(sshUsername);
    }

    public String getSshAuthType() {
        return sshAuthType.get();
    }

    public StringProperty sshAuthTypeProperty() {
        return sshAuthType;
    }

    public void setSshAuthType(String sshAuthType) {
        this.sshAuthType.set(sshAuthType);
    }

    public String getSshPrivateKeyFile() {
        return sshPrivateKeyFile.get();
    }

    public StringProperty sshPrivateKeyFileProperty() {
        return sshPrivateKeyFile;
    }

    public void setSshPrivateKeyFile(String sshPrivateKeyFile) {
        this.sshPrivateKeyFile.set(sshPrivateKeyFile);
    }

    public String getSshPass() {
        return sshPass.get();
    }

    public StringProperty sshPassProperty() {
        return sshPass;
    }

    public void setSshPass(String sshPass) {
        this.sshPass.set(sshPass);
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
