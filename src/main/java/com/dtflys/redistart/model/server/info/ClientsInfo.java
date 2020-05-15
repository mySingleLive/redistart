package com.dtflys.redistart.model.server.info;

public class ClientsInfo {

    private Integer connectedClients;

    private Integer clientLongestOutputList;

    private Integer clientBiggerInputBuf;

    private Integer blockedClients;

    public Integer getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(Integer connectedClients) {
        this.connectedClients = connectedClients;
    }

    public Integer getClientLongestOutputList() {
        return clientLongestOutputList;
    }

    public void setClientLongestOutputList(Integer clientLongestOutputList) {
        this.clientLongestOutputList = clientLongestOutputList;
    }

    public Integer getClientBiggerInputBuf() {
        return clientBiggerInputBuf;
    }

    public void setClientBiggerInputBuf(Integer clientBiggerInputBuf) {
        this.clientBiggerInputBuf = clientBiggerInputBuf;
    }

    public Integer getBlockedClients() {
        return blockedClients;
    }

    public void setBlockedClients(Integer blockedClients) {
        this.blockedClients = blockedClients;
    }
}
