package com.stackroute.datapopulator.domain;

import java.util.List;

public class ConvertedDataModel {
    private String userId;
    private String query;
    private List<ConvertedNode> nodes;
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

    public List<ConvertedNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ConvertedNode> nodes) {
        this.nodes = nodes;
    }

    public List<Relationship> getRelationship() {
        return relationship;
    }

    public void setRelationship(List<Relationship> relationship) {
        this.relationship = relationship;
    }
}
