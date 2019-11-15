package com.stackroute.queryservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Output {
    private String query;
    private String[] queryResult;
    private List<Map> constraints;
    private String strForDict;
    public String getStrForDict() {
        return strForDict;
    }
    public void setStrForDict(String strForDict) {
        this.strForDict = strForDict;
    }
    public Output() {
        constraints = new ArrayList<>();
    }
    public String[] getQueryResult() {
        return queryResult;
    }
    public void setQueryResult(String[] queryResult) {
        this.queryResult = queryResult;
    }
    public void setConstraint(List<Map> constraint)
    {
        this.constraints = constraint;
    }
    public List<Map> getConstraints() {
        return constraints;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
}
