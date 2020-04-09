package com.dtflys.redistart.script;

/**
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2018-06-23 15:59
 */
public class RedisScriptParserError extends RuntimeException {

    public RedisScriptParserError(String message) {
        super(message);
    }
}
