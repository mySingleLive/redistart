package com.dtflys.redistart.script;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.google.common.collect.Lists;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.BitSetCodec;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.IntegerCodec;
import org.redisson.client.codec.StringCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Redis命名脚本
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2017-08-09 16:31
 */
public class RedisNamedScript {

    private String name;
    private String sha;
    private RedisScriptSource source;
    private String script;
    private Boolean bModule;
    private String moduleName;
    private Map<String, RedisNamedScript> includedModules = new HashMap<>();

    public RedisNamedScript(String name, RedisScriptSource source, String script, Boolean bModule, String moduleName, Map<String, RedisNamedScript> includedModules) {
        this.name = name;
        this.source = source;
        this.script = script;
        this.bModule = bModule;
        this.moduleName = moduleName;
        this.includedModules = includedModules;
    }

    /**
     * 获得脚本名称
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 获得脚本在Redis中对应的SHA值
     * @return
     */
    public String getSha() {
        return sha;
    }

    /**
     * 是否为模块
     * @return
     */
    public Boolean isModule() {
        return bModule;
    }

    /**
     * 获取模块名称
     * @return
     */
    public String getModuleName() {
        return moduleName;
    }

    public Map<String, RedisNamedScript> getIncludedModules() {
        return includedModules;
    }

    /**
     * 获得脚本源文件内容
     * @return
     */
    public RedisScriptSource getSource() {
        return source;
    }

    /**
     * 获得脚本内容
     * @return
     */
    public String getScript() {
        return script;
    }

    /**
     * 加载本地脚本到Redis，并得到一个SHA值
     * @param redissonClient RedissonClient对象
     * @return SHA值
     */
    public String load(RedissonClient redissonClient) {
        if (bModule) {
            return null;
        }
        RScript rScript = redissonClient.getScript();
        try {
            String sha = rScript.scriptLoad(script);
            this.sha = sha;
            return sha;
        } catch (Throwable th) {
            throw new RedisScriptException("\n\n    An Exception occurred during pushing a redis script [" + source.getUrl().getPath() + "]:\n    Script:\n\n    " + script + "\n", th);
        }
    }

    public static class ScriptInfo {
        private RScript.ReturnType scriptReturnType;
        private Codec codec;

        public ScriptInfo(RScript.ReturnType scriptReturnType, Codec codec) {
            this.scriptReturnType = scriptReturnType;
            this.codec = codec;
        }

        public RScript.ReturnType getScriptReturnType() {
            return scriptReturnType;
        }

        public void setScriptReturnType(RScript.ReturnType scriptReturnType) {
            this.scriptReturnType = scriptReturnType;
        }

        public Codec getCodec() {
            return codec;
        }

        public void setCodec(Codec codec) {
            this.codec = codec;
        }
    }


    private ScriptInfo getRScriptInfo(Class returnType) {
        if (Boolean.class.isAssignableFrom(returnType)) {
            return new ScriptInfo(RScript.ReturnType.BOOLEAN, new BitSetCodec());
        }
        if (Integer.class.isAssignableFrom(returnType) || Long.class.isAssignableFrom(returnType)) {
            return new ScriptInfo(RScript.ReturnType.INTEGER, new IntegerCodec());
        }
        if (List.class.isAssignableFrom(returnType)) {
            if (JSONArray.class.isAssignableFrom(returnType)) {
                return new ScriptInfo(RScript.ReturnType.VALUE, new StringCodec());
            }
            return new ScriptInfo(RScript.ReturnType.MAPVALUELIST, new StringCodec());
        }
        if (Map.class.isAssignableFrom(returnType)) {
            return new ScriptInfo(RScript.ReturnType.VALUE, new StringCodec());
        }
        return new ScriptInfo(RScript.ReturnType.VALUE, new StringCodec());
    }

    /**
     * 执行脚本
     * @param redissonClient
     * @param returnType
     * @param mode
     * @param args
     * @param <T>
     * @return
     */
    public <T> T evalSHA(RedissonClient redissonClient, Class<T> returnType, RScript.Mode mode, Object... args)  {
        RScript rScript = redissonClient.getScript();
        try {
            ScriptInfo scriptInfo = getRScriptInfo(returnType);
            Object ret = rScript.evalSha(mode, scriptInfo.getCodec(), sha,
                    scriptInfo.getScriptReturnType(),
                    Lists.newArrayList(), args);
            T result = convertReturnObject(returnType, ret);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 执行脚本
     * @param redissonClient
     * @param returnType
     * @param mode
     * @param args
     * @param <T>
     * @return
     */
    public <T> T evalSHA(RedissonClient redissonClient, TypeReference<T> returnType, RScript.Mode mode, Object... args)  {
        RScript rScript = redissonClient.getScript();
        try {
            ScriptInfo scriptInfo = new ScriptInfo(RScript.ReturnType.VALUE, new StringCodec());
            Object ret = rScript.evalSha(mode, scriptInfo.getCodec(), sha,
                    scriptInfo.getScriptReturnType(),
                    Lists.newArrayList(), args);
            T result = convertReturnObject(returnType, ret);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }


    public <T> T eval(RedisConnection connection, Class<T> returnType, Object... args)  {
        try {
            Object ret = connection.eval(script, returnType, new String[0], args);
            T result = convertReturnObject(returnType, ret);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 转换返回对象类型
     * @param returnType
     * @param ret
     * @param <T>
     * @return
     */
    private <T> T convertReturnObject(Class<T> returnType, Object ret) {
        if (ret instanceof Long) {
            Long lret = (Long) ret;
            if (Integer.class.isAssignableFrom(returnType)) {
                return (T) Integer.valueOf(lret.intValue());
            }
            else if (Long.class.isAssignableFrom(returnType)) {
                return (T) lret;
            }
            else if (Short.class.isAssignableFrom(returnType)) {
                return (T) Short.valueOf(lret.shortValue());
            }
        }

        if (ret instanceof CharSequence) {
            String strRet = ret.toString();
            if (CharSequence.class.isAssignableFrom(returnType)) {
                return (T) strRet;
            }
            /*if (Map.class.isAssignableFrom(returnType)) {
                JSONObject json = null;
                try {
                    json = JSON.parseObject(strRet);
                } catch (Exception ex) {
                }
                if (json != null) {
                    return (T) json;
                }
            }
            if (List.class.isAssignableFrom(returnType)) {
                JSONArray json = null;
                try {
                    json =  JSON.parseArray(strRet);
                } catch (Exception ex) {
                }
                if (json != null) {
                    return (T) json;
                }
            }*/

            try {
                T javaRet = JSON.parseObject(strRet, returnType);
                return javaRet;
            } catch (Exception ex) {
            }
        }

        return (T) ret;
    }

    private <T> T convertReturnObject(TypeReference<T> returnType, Object ret) {
        if (ret instanceof CharSequence) {
            String strRet = ret.toString();
            try {
                T javaRet = JSON.parseObject(strRet, returnType);
                return javaRet;
            } catch (Exception ex) {
            }
        }
        return (T) ret;
    }


}
