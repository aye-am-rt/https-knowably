package com.stackroute.analyticsservice.service;

import com.stackroute.analyticsservice.repository.QueryResultResponse;
import com.stackroute.analyticsservice.domain.AnalyticsOutput;
import com.stackroute.analyticsservice.domain.Response;

import java.util.List;

public interface AnalyticsService {
    public boolean existsByQuery(String query);
    public void saveQuery(Response response);
    public void updateQuery(Response response);
    public List<QueryResultResponse> getResults();
    public List<QueryResultResponse> getResultsByDomain(String domain);
    public AnalyticsOutput getAnalyticsData();
}
