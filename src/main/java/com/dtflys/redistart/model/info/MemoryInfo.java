package com.dtflys.redistart.model.info;

public class MemoryInfo {

    private Long usedMemory;

    private Long usedMemoryRSS;

    private Long usedMemoryPeak;

    private Long totalSystemMemory;

    private Long usedMemoryLua;

    private Long maxMemory;

    private String maxMemoryPolicy;

    private Double memFragmentationRatio;

    private String memAllocator;

    public Long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public Long getUsedMemoryRSS() {
        return usedMemoryRSS;
    }

    public void setUsedMemoryRSS(Long usedMemoryRSS) {
        this.usedMemoryRSS = usedMemoryRSS;
    }

    public Long getUsedMemoryPeak() {
        return usedMemoryPeak;
    }

    public void setUsedMemoryPeak(Long usedMemoryPeak) {
        this.usedMemoryPeak = usedMemoryPeak;
    }

    public Long getTotalSystemMemory() {
        return totalSystemMemory;
    }

    public void setTotalSystemMemory(Long totalSystemMemory) {
        this.totalSystemMemory = totalSystemMemory;
    }

    public Long getUsedMemoryLua() {
        return usedMemoryLua;
    }

    public void setUsedMemoryLua(Long usedMemoryLua) {
        this.usedMemoryLua = usedMemoryLua;
    }

    public Long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(Long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public String getMaxMemoryPolicy() {
        return maxMemoryPolicy;
    }

    public void setMaxMemoryPolicy(String maxMemoryPolicy) {
        this.maxMemoryPolicy = maxMemoryPolicy;
    }

    public Double getMemFragmentationRatio() {
        return memFragmentationRatio;
    }

    public void setMemFragmentationRatio(Double memFragmentationRatio) {
        this.memFragmentationRatio = memFragmentationRatio;
    }

    public String getMemAllocator() {
        return memAllocator;
    }

    public void setMemAllocator(String memAllocator) {
        this.memAllocator = memAllocator;
    }
}
