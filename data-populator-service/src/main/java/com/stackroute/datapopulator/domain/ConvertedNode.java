package com.stackroute.datapopulator.domain;

import java.util.List;
import java.util.Map;

public class ConvertedNode {
    private String uuid;
    private List<String> type;
    private Map<String, String> properties;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
