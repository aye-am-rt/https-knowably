package com.stackroute.resultfetcher.domain;

import java.util.List;
import java.util.Map;

public class ProcessedQueryModel {
    private String query;
    private String domain;
    private List<String> queryresult;
    private List<Map> constraints;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getQueryresult() {
        return queryresult;
    }

    public void setQueryresult(List<String> queryresult) {
        this.queryresult = queryresult;
    }

    public List<Map> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Map> constraints) {
        this.constraints = constraints;
    }
}
