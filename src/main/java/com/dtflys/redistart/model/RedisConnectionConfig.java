package com.dtflys.redistart.model;

public class RedisConnectionConfig {

    /* Connection Settings */

    private String name;

    private String redisHost;

    private Integer redisPort;

    private String redisPassword;

    /* SSL Settings */

    private Boolean isUseSSL;

    /* SSH Settings */

    private Boolean isUseSSH;

    /* Advanced Settings */

    private Integer timeout;

    private Integer connectTimeout;

    private Integer reconnectTimeout;

    private Integer connectionPoolSize;

    private Integer queryPageSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public Boolean getUseSSL() {
        return isUseSSL;
    }

    public void setUseSSL(Boolean useSSL) {
        isUseSSL = useSSL;
    }

    public Boolean getUseSSH() {
        return isUseSSH;
    }

    public void setUseSSH(Boolean useSSH) {
        isUseSSH = useSSH;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReconnectTimeout() {
        return reconnectTimeout;
    }

    public void setReconnectTimeout(Integer reconnectTimeout) {
        this.reconnectTimeout = reconnectTimeout;
    }

    public Integer getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(Integer connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public Integer getQueryPageSize() {
        return queryPageSize;
    }

    public void setQueryPageSize(Integer queryPageSize) {
        this.queryPageSize = queryPageSize;
    }
}
