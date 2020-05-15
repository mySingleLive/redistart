package com.dtflys.redistart.model.server.info;

public class StatsInfo {

    // 自启动起连接过的总数
    private Integer totalConnectionsReceived;

    // 自启动起运行命令的总数
    private Integer totalCommandsProcessed;

    // 每秒执行的命令个数
    private Integer instantaneousOpsPerSec;

    // 因为最大客户端连接书限制，而导致被拒绝连接的个数
    private Integer rejectedConnections;

    // 自启动起过期的key的总数
    private Integer expiredKeys;

    // 因为内存大小限制，而被驱逐出去的键的个数
    private Integer evictedKeys;

    // 在main dictionary(todo)中成功查到的key个数
    private Integer keyspaceHits;

    // 同上，未查到的key的个数
    private Integer keyspaceMisses;

    // 发布/订阅频道数
    private Integer pubsubChannels;

    // 发布/订阅模式数
    private Integer pubsubPatterns;

    public Integer getTotalConnectionsReceived() {
        return totalConnectionsReceived;
    }

    public void setTotalConnectionsReceived(Integer totalConnectionsReceived) {
        this.totalConnectionsReceived = totalConnectionsReceived;
    }

    public Integer getTotalCommandsProcessed() {
        return totalCommandsProcessed;
    }

    public void setTotalCommandsProcessed(Integer totalCommandsProcessed) {
        this.totalCommandsProcessed = totalCommandsProcessed;
    }

    public Integer getInstantaneousOpsPerSec() {
        return instantaneousOpsPerSec;
    }

    public void setInstantaneousOpsPerSec(Integer instantaneousOpsPerSec) {
        this.instantaneousOpsPerSec = instantaneousOpsPerSec;
    }

    public Integer getRejectedConnections() {
        return rejectedConnections;
    }

    public void setRejectedConnections(Integer rejectedConnections) {
        this.rejectedConnections = rejectedConnections;
    }

    public Integer getExpiredKeys() {
        return expiredKeys;
    }

    public void setExpiredKeys(Integer expiredKeys) {
        this.expiredKeys = expiredKeys;
    }

    public Integer getEvictedKeys() {
        return evictedKeys;
    }

    public void setEvictedKeys(Integer evictedKeys) {
        this.evictedKeys = evictedKeys;
    }

    public Integer getKeyspaceHits() {
        return keyspaceHits;
    }

    public void setKeyspaceHits(Integer keyspaceHits) {
        this.keyspaceHits = keyspaceHits;
    }

    public Integer getKeyspaceMisses() {
        return keyspaceMisses;
    }

    public void setKeyspaceMisses(Integer keyspaceMisses) {
        this.keyspaceMisses = keyspaceMisses;
    }

    public Integer getPubsubChannels() {
        return pubsubChannels;
    }

    public void setPubsubChannels(Integer pubsubChannels) {
        this.pubsubChannels = pubsubChannels;
    }

    public Integer getPubsubPatterns() {
        return pubsubPatterns;
    }

    public void setPubsubPatterns(Integer pubsubPatterns) {
        this.pubsubPatterns = pubsubPatterns;
    }
}
