package com.dtflys.redistart.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("redisConnectionConfigStorage")
public class RedisConnectionConfigStorage extends RSAbstractStorage {

    private final static String DIR_PATH = "data";
    private final static String FILE_NAME = "C_DATA.data";

    public void insertConnectionConfigList(List<RedisConnectionConfig> connectionConfigList) {
        List<RedisConnectionConfig> list = loadConnectionConfigList();
        if (list == null) {
            list = Lists.newArrayList();
        }
        list.addAll(connectionConfigList);
        saveConnectionConfigList(list);
    }

    public void deleteConnectionConfigList(List<RedisConnectionConfig> connectionConfigList) {
        List<RedisConnectionConfig> list = loadConnectionConfigList();
        if (list == null) {
            list = Lists.newArrayList();
        }
        List<RedisConnectionConfig> toDelList = new ArrayList<>();
        for (RedisConnectionConfig connectionConfig : list) {
            for (RedisConnectionConfig delConfig : connectionConfigList) {
                if (connectionConfig.getId().equals(delConfig.getId())) {
                    toDelList.add(connectionConfig);
                }
            }
        }
        list.removeAll(toDelList);
        saveConnectionConfigList(list);
    }

    public void updateAllConnectionConfigList(List<RedisConnectionConfig> connectionConfigList) {
        List<RedisConnectionConfig> list = loadConnectionConfigList();
        if (list == null) {
            list = Lists.newArrayList();
        }
        for (RedisConnectionConfig connectionConfig : list) {
            for (RedisConnectionConfig updateConfig : connectionConfigList) {
                if (connectionConfig.getId().equals(updateConfig.getId())) {
                    BeanUtils.copyProperties(updateConfig, connectionConfig);
                }
            }
        }
        saveConnectionConfigList(list);
    }



    public void saveConnectionConfigList(List<RedisConnectionConfig> connectionConfigList) {
        String jsonStr = JSON.toJSONString(connectionConfigList);
        save(DIR_PATH, FILE_NAME, jsonStr);
    }

    public List<RedisConnectionConfig> loadConnectionConfigList() {
        String jsonStr = loadStorageAsString(DIR_PATH, FILE_NAME);
        if (StringUtils.isBlank(jsonStr)) {
            return Lists.newArrayList();
        }
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        List<RedisConnectionConfig> list = jsonArray.toJavaList(RedisConnectionConfig.class);
        return list;
    }

}
