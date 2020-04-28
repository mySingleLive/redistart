package com.dtflys.redistart.model.lua;

public enum RSResultCode {

    SUCCESS("成功"),
    KEY_EXIST("该Key已存在")

    ;

    private final String msg;

    RSResultCode(String msg) {
        this.msg = msg;
    }
}
