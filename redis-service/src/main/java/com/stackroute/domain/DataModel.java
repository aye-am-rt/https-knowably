package com.stackroute.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
@RedisHash("DataModel")
public class DataModel implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    @Id
    private String key;
    private List<String> value;

    public DataModel(String key, List<String> value) {
        this.key = key;
        this.value = value;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
