package com.dtflys.redistart.model.server.info;

public class ServerInfo {

    private String redisVersion;

    private String redisGitSHA1;

    private Integer redisGitDirty;

    private String redisMode;

    private String os;

    private Integer archBits;

    private String multiplexingApi;

    private String gccVersion;

    private Long processId;

    private String runId;

    private Integer tcpPort;

    private Integer uptimeInSeconds;

    private Integer uptimeInDays;

    private Long lruClock;

    public String getRedisVersion() {
        return redisVersion;
    }

    public void setRedisVersion(String redisVersion) {
        this.redisVersion = redisVersion;
    }

    public String getRedisGitSHA1() {
        return redisGitSHA1;
    }

    public void setRedisGitSHA1(String redisGitSHA1) {
        this.redisGitSHA1 = redisGitSHA1;
    }

    public Integer getRedisGitDirty() {
        return redisGitDirty;
    }

    public void setRedisGitDirty(Integer redisGitDirty) {
        this.redisGitDirty = redisGitDirty;
    }

    public String getRedisMode() {
        return redisMode;
    }

    public void setRedisMode(String redisMode) {
        this.redisMode = redisMode;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Integer getArchBits() {
        return archBits;
    }

    public void setArchBits(Integer archBits) {
        this.archBits = archBits;
    }

    public String getMultiplexingApi() {
        return multiplexingApi;
    }

    public void setMultiplexingApi(String multiplexingApi) {
        this.multiplexingApi = multiplexingApi;
    }

    public String getGccVersion() {
        return gccVersion;
    }

    public void setGccVersion(String gccVersion) {
        this.gccVersion = gccVersion;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public Integer getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(Integer tcpPort) {
        this.tcpPort = tcpPort;
    }

    public Integer getUptimeInSeconds() {
        return uptimeInSeconds;
    }

    public void setUptimeInSeconds(Integer uptimeInSeconds) {
        this.uptimeInSeconds = uptimeInSeconds;
    }

    public Integer getUptimeInDays() {
        return uptimeInDays;
    }

    public void setUptimeInDays(Integer uptimeInDays) {
        this.uptimeInDays = uptimeInDays;
    }

    public Long getLruClock() {
        return lruClock;
    }

    public void setLruClock(Long lruClock) {
        this.lruClock = lruClock;
    }
}
