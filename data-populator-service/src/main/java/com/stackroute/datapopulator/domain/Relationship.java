package com.stackroute.datapopulator.domain;

import java.util.Map;

public class Relationship {
    private String uuid;
    private String relation;
    private String sourcenode;
    private String destnode;
    private Map<String, String> properties;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getSourcenode() {
        return sourcenode;
    }

    public void setSourcenode(String sourcenode) {
        this.sourcenode = sourcenode;
    }

    public String getDestnode() {
        return destnode;
    }

    public void setDestnode(String destnode) {
        this.destnode = destnode;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
