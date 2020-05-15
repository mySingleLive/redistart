package com.dtflys.redistart.model.server.info;

public class CpuInfo {

    // redis server的sys cpu使用率
    private Float usedCpuSys;

    // redis server的user cpu使用率
    private Float usedCpuUser;

    // 后台进程的sys cpu使用率
    private Float usedCpuSysChildren;

    // 后台进程的user cpu使用率
    private Float usedCpuUserChildren;

    public Float getUsedCpuSys() {
        return usedCpuSys;
    }

    public void setUsedCpuSys(Float usedCpuSys) {
        this.usedCpuSys = usedCpuSys;
    }

    public Float getUsedCpuUser() {
        return usedCpuUser;
    }

    public void setUsedCpuUser(Float usedCpuUser) {
        this.usedCpuUser = usedCpuUser;
    }

    public Float getUsedCpuSysChildren() {
        return usedCpuSysChildren;
    }

    public void setUsedCpuSysChildren(Float usedCpuSysChildren) {
        this.usedCpuSysChildren = usedCpuSysChildren;
    }

    public Float getUsedCpuUserChildren() {
        return usedCpuUserChildren;
    }

    public void setUsedCpuUserChildren(Float usedCpuUserChildren) {
        this.usedCpuUserChildren = usedCpuUserChildren;
    }
}
