package com.stackroute.analyticsservice.service;
import com.stackroute.analyticsservice.domain.Response;
import com.stackroute.analyticsservice.repository.QueryResultResponse;
import com.stackroute.analyticsservice.repository.QueryResultResponseKey;
import com.stackroute.analyticsservice.repository.QueryResultResponseRepository;
import com.stackroute.analyticsservice.domain.AnalyticsOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService{

    private QueryResultResponseRepository queryResultResponseRepository;
    @Autowired
    public AnalyticsServiceImpl(QueryResultResponseRepository queryResultResponseRepository) {
        this.queryResultResponseRepository = queryResultResponseRepository;
    }
    //Checks if response exists in Cassandra
    @Override
    public boolean existsByQuery(String query) {
        return queryResultResponseRepository.existsByKeyQuery(query);
    }
    //Saves response to Cassandra
    @Override
    public void saveQuery(Response response) {
        QueryResultResponseKey key = new QueryResultResponseKey();
        key.setDomain(response.getDomain());
        key.setQuery(response.getQuery());
        if(response.getResponse().equals("accurate"))
        {
            key.setPosResponse(1);
        }
        else if(response.getResponse().equals("inaccurate"))
        {
            key.setNegResponse(1);
        }
        QueryResultResponse resultResponse = new QueryResultResponse();
        resultResponse.setKey(key);
        resultResponse.setResult(response.getResult());
        queryResultResponseRepository.insert(resultResponse);
    }
    //Updates response in Cassandra
    @Override
    public void updateQuery(Response response) {
        QueryResultResponse resultResponse = queryResultResponseRepository.findByKeyQuery(response.getQuery());
        queryResultResponseRepository.delete(resultResponse);
        QueryResultResponseKey key = new QueryResultResponseKey();
        key.setQuery(response.getQuery());
        key.setDomain(response.getDomain());
        key.setPosResponse(resultResponse.getKey().getPosResponse());
        key.setNegResponse(resultResponse.getKey().getNegResponse());
        if(response.getResponse().equals("accurate"))
        {
            key.setPosResponse(key.getPosResponse()+1);
        }
        else if(response.getResponse().equals("inaccurate"))
        {
            key.setNegResponse(key.getNegResponse()+1);
        }
        resultResponse.setKey(key);
        queryResultResponseRepository.insert(resultResponse);
    }
    //Returns all responses from Cassandra
    @Override
    public List<QueryResultResponse> getResults() {
        return queryResultResponseRepository.findAll();
    }
    //Returns responses by domain from Cassandra
    @Override
    public List<QueryResultResponse> getResultsByDomain(String domain) {
        return queryResultResponseRepository.findByKeyDomain(domain);
    }
    //Calculates and returns analytics data from Cassandra
    @Override
    public AnalyticsOutput getAnalyticsData() {
        AnalyticsOutput output = new AnalyticsOutput();
        List<QueryResultResponse> mov = getResultsByDomain("movie");
        List<QueryResultResponse> med = getResultsByDomain("medical");
        int movpr=0;
        int movnr=0;
        int medpr=0;
        int mednr=0;
        //Positive and negative responses for movies
        for(QueryResultResponse a: mov)
        {
            movpr = movpr + a.getKey().getPosResponse();
            movnr = movnr + a.getKey().getNegResponse();
        }
        //Positive and negative responses for medical
        for(QueryResultResponse b: med)
        {
            medpr = medpr + b.getKey().getPosResponse();
            mednr = mednr + b.getKey().getNegResponse();
        }
        output.setMovPosResp(movpr);
        output.setMovNegResp(movnr);
        output.setMedPosResp(medpr);
        output.setMedNegResp(mednr);
        //Accuracy for movie and medical domains
        if((movnr + movpr) == 0 || (mednr+medpr)==0){
            output.setMedAcc(0.0);
            output.setMedAcc(0.0);
        }else {
            output.setMovAcc(((double) movpr / (movnr + movpr)) * 100.0);
            output.setMedAcc(((double) medpr / (mednr + medpr)) * 100.0);
        }
        return output;
    }
}
