package com.dtflys.redistart.script;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis脚本管理器
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2017-08-09 15:08
 */
public class RedisScriptManager {

    private final static Logger log = LoggerFactory.getLogger(RedisScriptManager.class);

    private final static RedisScriptManager instance = new RedisScriptManager();

    private Map<String, RedisNamedScript> namedScriptMap = new HashMap<>();

    private Map<String, RedisScriptSource> scriptSourceMap = new HashMap<>();


    public static RedisScriptManager scriptManager() {
        return instance;
    }

    public RedisScriptManager() {
    }

    /**
     * 根据名称获得脚本对象
     * @param name
     * @return
     */
    public RedisNamedScript getNamedScript(String name) {
        return namedScriptMap.get(name);
    }

    /**
     * 添加一个命名脚本对象
     * @param namedScript
     */
    public void addNamedScript(RedisNamedScript namedScript) {
        namedScriptMap.put(namedScript.getName(), namedScript);
    }


    /**
     * 加载脚本源文件
     * @param url
     * @return
     * @throws IOException
     */
    public RedisScriptSource loadScriptSource(URL url) throws IOException {
        log.info(" [Redis] 加载Lua源文件：" + url.getPath());
        String path = url.getPath();
        RedisScriptSource source = scriptSourceMap.get(path);
        if (source == null) {
            source = new RedisScriptSource(url);
            scriptSourceMap.put(path, source);
            source.read();
        }
        return source;
    }

    public RedisScriptSource loadScriptSource(String path) throws IOException {
        return loadScriptSource(new URL(path));
    }


    /**
     * 加载脚本
     * @param source
     * @throws IOException
     */
    public RedisNamedScript loadScript(RedisScriptSource source) throws IOException {
        RedisScriptParser parser = new RedisScriptParser(this, source);
        // 解析脚本源文件
        RedisNamedScript namedScript = parser.parse();
        this.addNamedScript(namedScript);
        return namedScript;
    }

    public void logScriptInfo(String name) {
        RedisNamedScript script = getNamedScript(name);
        log.info("\n[Redis] Script Info:\n" +
                "[Name: " + script.getName() + "]\n" +
                "[Path: " + script.getSource().getUrl().getPath() + "]\n" +
                "[SHA: " + script.getSha() + "]" +
                "[编译结果]: \n\n" + script.getScript() + "\n\n");
    }

    /**
     * 将脚本推到Redis服务端
     * @param name
     */
    public void pushScriptToRedisServer(RedissonClient redissonClient, String name) {
        RedisNamedScript script = getNamedScript(name);
        script.load(redissonClient);
        log.info("\n[Redis] Script Info:\n" +
                "[Name: " + script.getName() + "]\n" +
                "[Path: " + script.getSource().getUrl().getPath() + "]\n" +
                "[SHA: " + script.getSha() + "]");
    }

    /**
     * 将所有脚本推到Redis服务端
     * @param redissonClient
     */
    public void pushAllScriptsToRedisServer(RedissonClient redissonClient) {
        for (String name : namedScriptMap.keySet()) {
            pushScriptToRedisServer(redissonClient, name);
        }
    }

    /**
     * 加载脚本
     * @param url
     * @return
     * @throws IOException
     */
    public RedisNamedScript loadScript(URL url) throws IOException {
        // 加载lua源文件
        RedisScriptSource source = loadScriptSource(url);
        // 记载脚本
        RedisNamedScript script = loadScript(source);
        return script;
    }



}
