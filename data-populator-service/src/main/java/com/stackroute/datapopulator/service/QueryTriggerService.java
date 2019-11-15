package com.stackroute.datapopulator.service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
public class QueryTriggerService {
    private static final Logger logger = LoggerFactory.getLogger(QueryTriggerService.class);

    public void queryServiceTrigger(String query) throws IOException {
        URL object=new URL("http://13.127.108.14:8087/api/v1/query");
        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        logger.error("Http connection");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");
        JSONObject cred   = new JSONObject();
        cred.put("query",query);
        OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
        wr.write(cred.toString());
        wr.flush();
        wr.close();
    }
}
