package com.dtflys.redistart.model.server.config;

public class RedisServerConfig {
    private String dbfilename;
    private String requirepass;
    private String masterauth;
    private String unixsocket;
    private String logfile;
    private String pidfile;
    private String maxmemory;
    private String maxmemorySamples;
    private String timeout;
    private String tcpKeepalive;
    private String autoAofRewritePercentage;
    private String autoAofRewriteMinSize;
    private String hashMaxZiplistEntries;
    private String hashMaxZiplistValue;
    private String listMaxZiplistEntries;
    private String listMaxZiplistValue;
    private String setMaxIntsetEntries;
    private String zsetMaxZiplistEntries;
    private String zsetMaxZiplistValue;
    private String hllSparseMaxBytes;
    private String luaTimeLimit;
    private String slowlogLogSlowerThan;
    private String latencyMonitorThreshold;
    private String slowlogMaxLen;
    private String port;
    private String tcpBacklog;
    private String databases;
    private String replPingSlavePeriod;
    private String replTimeout;
    private String replBacklogSize;
    private String replBacklogTtl;
    private String maxclients;
    private String watchdogPeriod;
    private String slavePriority;
    private String minSlavesToWrite;
    private String minSlavesMaxLag;
    private String hz;
    private String noAppendfsyncOnRewrite;
    private String slaveServeStaleData;
    private String slaveReadOnly;
    private String stopWritesOnBgsaveError;
    private String daemonize;
    private String rdbcompression;
    private String rdbchecksum;
    private String activerehashing;
    private String replDisableTcpNodelay;
    private String aofRewriteIncrementalFsync;
    private String appendonly;
    private String dir;
    private String maxmemoryPolicy;
    private String appendfsync;
    private String save;
    private String loglevel;
    private String clientOutputBufferLimit;
    private String unixsocketperm;
    private String slaveof;
    private String notifyKeyspaceEvents;
    private String bind;

    public String getDbfilename() {
        return dbfilename;
    }

    public void setDbfilename(String dbfilename) {
        this.dbfilename = dbfilename;
    }

    public String getRequirepass() {
        return requirepass;
    }

    public void setRequirepass(String requirepass) {
        this.requirepass = requirepass;
    }

    public String getMasterauth() {
        return masterauth;
    }

    public void setMasterauth(String masterauth) {
        this.masterauth = masterauth;
    }

    public String getUnixsocket() {
        return unixsocket;
    }

    public void setUnixsocket(String unixsocket) {
        this.unixsocket = unixsocket;
    }

    public String getLogfile() {
        return logfile;
    }

    public void setLogfile(String logfile) {
        this.logfile = logfile;
    }

    public String getPidfile() {
        return pidfile;
    }

    public void setPidfile(String pidfile) {
        this.pidfile = pidfile;
    }

    public String getMaxmemory() {
        return maxmemory;
    }

    public void setMaxmemory(String maxmemory) {
        this.maxmemory = maxmemory;
    }

    public String getMaxmemorySamples() {
        return maxmemorySamples;
    }

    public void setMaxmemorySamples(String maxmemorySamples) {
        this.maxmemorySamples = maxmemorySamples;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getTcpKeepalive() {
        return tcpKeepalive;
    }

    public void setTcpKeepalive(String tcpKeepalive) {
        this.tcpKeepalive = tcpKeepalive;
    }

    public String getAutoAofRewritePercentage() {
        return autoAofRewritePercentage;
    }

    public void setAutoAofRewritePercentage(String autoAofRewritePercentage) {
        this.autoAofRewritePercentage = autoAofRewritePercentage;
    }

    public String getAutoAofRewriteMinSize() {
        return autoAofRewriteMinSize;
    }

    public void setAutoAofRewriteMinSize(String autoAofRewriteMinSize) {
        this.autoAofRewriteMinSize = autoAofRewriteMinSize;
    }

    public String getHashMaxZiplistEntries() {
        return hashMaxZiplistEntries;
    }

    public void setHashMaxZiplistEntries(String hashMaxZiplistEntries) {
        this.hashMaxZiplistEntries = hashMaxZiplistEntries;
    }

    public String getHashMaxZiplistValue() {
        return hashMaxZiplistValue;
    }

    public void setHashMaxZiplistValue(String hashMaxZiplistValue) {
        this.hashMaxZiplistValue = hashMaxZiplistValue;
    }

    public String getListMaxZiplistEntries() {
        return listMaxZiplistEntries;
    }

    public void setListMaxZiplistEntries(String listMaxZiplistEntries) {
        this.listMaxZiplistEntries = listMaxZiplistEntries;
    }

    public String getListMaxZiplistValue() {
        return listMaxZiplistValue;
    }

    public void setListMaxZiplistValue(String listMaxZiplistValue) {
        this.listMaxZiplistValue = listMaxZiplistValue;
    }

    public String getSetMaxIntsetEntries() {
        return setMaxIntsetEntries;
    }

    public void setSetMaxIntsetEntries(String setMaxIntsetEntries) {
        this.setMaxIntsetEntries = setMaxIntsetEntries;
    }

    public String getZsetMaxZiplistEntries() {
        return zsetMaxZiplistEntries;
    }

    public void setZsetMaxZiplistEntries(String zsetMaxZiplistEntries) {
        this.zsetMaxZiplistEntries = zsetMaxZiplistEntries;
    }

    public String getZsetMaxZiplistValue() {
        return zsetMaxZiplistValue;
    }

    public void setZsetMaxZiplistValue(String zsetMaxZiplistValue) {
        this.zsetMaxZiplistValue = zsetMaxZiplistValue;
    }

    public String getHllSparseMaxBytes() {
        return hllSparseMaxBytes;
    }

    public void setHllSparseMaxBytes(String hllSparseMaxBytes) {
        this.hllSparseMaxBytes = hllSparseMaxBytes;
    }

    public String getLuaTimeLimit() {
        return luaTimeLimit;
    }

    public void setLuaTimeLimit(String luaTimeLimit) {
        this.luaTimeLimit = luaTimeLimit;
    }

    public String getSlowlogLogSlowerThan() {
        return slowlogLogSlowerThan;
    }

    public void setSlowlogLogSlowerThan(String slowlogLogSlowerThan) {
        this.slowlogLogSlowerThan = slowlogLogSlowerThan;
    }

    public String getLatencyMonitorThreshold() {
        return latencyMonitorThreshold;
    }

    public void setLatencyMonitorThreshold(String latencyMonitorThreshold) {
        this.latencyMonitorThreshold = latencyMonitorThreshold;
    }

    public String getSlowlogMaxLen() {
        return slowlogMaxLen;
    }

    public void setSlowlogMaxLen(String slowlogMaxLen) {
        this.slowlogMaxLen = slowlogMaxLen;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTcpBacklog() {
        return tcpBacklog;
    }

    public void setTcpBacklog(String tcpBacklog) {
        this.tcpBacklog = tcpBacklog;
    }

    public String getDatabases() {
        return databases;
    }

    public void setDatabases(String databases) {
        this.databases = databases;
    }

    public String getReplPingSlavePeriod() {
        return replPingSlavePeriod;
    }

    public void setReplPingSlavePeriod(String replPingSlavePeriod) {
        this.replPingSlavePeriod = replPingSlavePeriod;
    }

    public String getReplTimeout() {
        return replTimeout;
    }

    public void setReplTimeout(String replTimeout) {
        this.replTimeout = replTimeout;
    }

    public String getReplBacklogSize() {
        return replBacklogSize;
    }

    public void setReplBacklogSize(String replBacklogSize) {
        this.replBacklogSize = replBacklogSize;
    }

    public String getReplBacklogTtl() {
        return replBacklogTtl;
    }

    public void setReplBacklogTtl(String replBacklogTtl) {
        this.replBacklogTtl = replBacklogTtl;
    }

    public String getMaxclients() {
        return maxclients;
    }

    public void setMaxclients(String maxclients) {
        this.maxclients = maxclients;
    }

    public String getWatchdogPeriod() {
        return watchdogPeriod;
    }

    public void setWatchdogPeriod(String watchdogPeriod) {
        this.watchdogPeriod = watchdogPeriod;
    }

    public String getSlavePriority() {
        return slavePriority;
    }

    public void setSlavePriority(String slavePriority) {
        this.slavePriority = slavePriority;
    }

    public String getMinSlavesToWrite() {
        return minSlavesToWrite;
    }

    public void setMinSlavesToWrite(String minSlavesToWrite) {
        this.minSlavesToWrite = minSlavesToWrite;
    }

    public String getMinSlavesMaxLag() {
        return minSlavesMaxLag;
    }

    public void setMinSlavesMaxLag(String minSlavesMaxLag) {
        this.minSlavesMaxLag = minSlavesMaxLag;
    }

    public String getHz() {
        return hz;
    }

    public void setHz(String hz) {
        this.hz = hz;
    }

    public String getNoAppendfsyncOnRewrite() {
        return noAppendfsyncOnRewrite;
    }

    public void setNoAppendfsyncOnRewrite(String noAppendfsyncOnRewrite) {
        this.noAppendfsyncOnRewrite = noAppendfsyncOnRewrite;
    }

    public String getSlaveServeStaleData() {
        return slaveServeStaleData;
    }

    public void setSlaveServeStaleData(String slaveServeStaleData) {
        this.slaveServeStaleData = slaveServeStaleData;
    }

    public String getSlaveReadOnly() {
        return slaveReadOnly;
    }

    public void setSlaveReadOnly(String slaveReadOnly) {
        this.slaveReadOnly = slaveReadOnly;
    }

    public String getStopWritesOnBgsaveError() {
        return stopWritesOnBgsaveError;
    }

    public void setStopWritesOnBgsaveError(String stopWritesOnBgsaveError) {
        this.stopWritesOnBgsaveError = stopWritesOnBgsaveError;
    }

    public String getDaemonize() {
        return daemonize;
    }

    public void setDaemonize(String daemonize) {
        this.daemonize = daemonize;
    }

    public String getRdbcompression() {
        return rdbcompression;
    }

    public void setRdbcompression(String rdbcompression) {
        this.rdbcompression = rdbcompression;
    }

    public String getRdbchecksum() {
        return rdbchecksum;
    }

    public void setRdbchecksum(String rdbchecksum) {
        this.rdbchecksum = rdbchecksum;
    }

    public String getActiverehashing() {
        return activerehashing;
    }

    public void setActiverehashing(String activerehashing) {
        this.activerehashing = activerehashing;
    }

    public String getReplDisableTcpNodelay() {
        return replDisableTcpNodelay;
    }

    public void setReplDisableTcpNodelay(String replDisableTcpNodelay) {
        this.replDisableTcpNodelay = replDisableTcpNodelay;
    }

    public String getAofRewriteIncrementalFsync() {
        return aofRewriteIncrementalFsync;
    }

    public void setAofRewriteIncrementalFsync(String aofRewriteIncrementalFsync) {
        this.aofRewriteIncrementalFsync = aofRewriteIncrementalFsync;
    }

    public String getAppendonly() {
        return appendonly;
    }

    public void setAppendonly(String appendonly) {
        this.appendonly = appendonly;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getMaxmemoryPolicy() {
        return maxmemoryPolicy;
    }

    public void setMaxmemoryPolicy(String maxmemoryPolicy) {
        this.maxmemoryPolicy = maxmemoryPolicy;
    }

    public String getAppendfsync() {
        return appendfsync;
    }

    public void setAppendfsync(String appendfsync) {
        this.appendfsync = appendfsync;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }

    public String getClientOutputBufferLimit() {
        return clientOutputBufferLimit;
    }

    public void setClientOutputBufferLimit(String clientOutputBufferLimit) {
        this.clientOutputBufferLimit = clientOutputBufferLimit;
    }

    public String getUnixsocketperm() {
        return unixsocketperm;
    }

    public void setUnixsocketperm(String unixsocketperm) {
        this.unixsocketperm = unixsocketperm;
    }

    public String getSlaveof() {
        return slaveof;
    }

    public void setSlaveof(String slaveof) {
        this.slaveof = slaveof;
    }

    public String getNotifyKeyspaceEvents() {
        return notifyKeyspaceEvents;
    }

    public void setNotifyKeyspaceEvents(String notifyKeyspaceEvents) {
        this.notifyKeyspaceEvents = notifyKeyspaceEvents;
    }

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }
}
