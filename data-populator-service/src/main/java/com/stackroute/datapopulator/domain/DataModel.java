package com.stackroute.datapopulator.domain;

import java.util.List;

public class DataModel {
    private String userId;
    private String query;
    private List<Node> nodes;
    private List<Relationship> relationship;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Relationship> getRelationship() {
        return relationship;
    }

    public void setRelationship(List<Relationship> relationship) {
        this.relationship = relationship;
    }
}
