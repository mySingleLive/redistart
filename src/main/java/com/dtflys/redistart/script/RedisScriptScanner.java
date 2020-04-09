package com.dtflys.redistart.script;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Redis脚本扫描器
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2017-08-09 14:41
 */
public class RedisScriptScanner implements InitializingBean {

    private final static Logger log = LoggerFactory.getLogger(RedisScriptScanner.class);

    private String location;


    private final RedisScriptManager scriptManager = RedisScriptManager.scriptManager();

    public RedisScriptScanner() {
    }

    public RedisScriptScanner(String location) {
        this.location = location;
        try{
            scan();
        }catch (Throwable t){
            log.error("cannot init redis scrip scanner",t);
            throw new RuntimeException("cannot init redis scrip scanner");
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    /**
     * 扫描项目的[location指定地址目录下]所有Lua脚本文件。 </BR>
     * 每当扫描到一个Lua脚本文件，便会添加到脚本管理器中，</BR>
     * 每个Lua脚本文件都有一个名称，就是该脚本的文件名。</BR>
     * @throws IOException
     */
    public void scan() throws IOException {
        if (StringUtils.isBlank(location)) {
            return;
        }
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(location);

        for (Resource resource : resources) {
            // 加载lua源文件
            RedisScriptSource source = scriptManager.loadScriptSource(resource.getURL());
            // 加载脚本
            scriptManager.loadScript(source);
        }
        // 将所有脚本推到Redis服务端
//        scriptManager.pushAllScriptsToRedisServer(redissonClient);
    }


    private String getScriptName(String filename) {
        String[] segments = filename.split("/|\\\\");
        return segments[segments.length - 1];
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scan();
    }
}
