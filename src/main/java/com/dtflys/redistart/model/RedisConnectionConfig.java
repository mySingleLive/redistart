package com.dtflys.redistart.model;

public class RedisConnectionConfig {

    private String name;

    private String redisHost;

    private String redisPort;

    private String redisPassword;

    private Boolean isUseSSL;

    private Boolean isUesSSH;

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

    public String getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(String redisPort) {
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

    public Boolean getUesSSH() {
        return isUesSSH;
    }

    public void setUesSSH(Boolean uesSSH) {
        isUesSSH = uesSSH;
    }
}
