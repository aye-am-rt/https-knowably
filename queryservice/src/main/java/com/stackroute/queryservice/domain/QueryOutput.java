package com.stackroute.queryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryOutput {
    private String domain;
    private String query;
    private List<String> queryresult;
    private List<Map> constraints;
}
