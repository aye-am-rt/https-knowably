package com.stackroute.analyticsservice.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@PrimaryKeyClass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResultResponseKey implements Serializable {

    @PrimaryKeyColumn(name="query",type = PrimaryKeyType.PARTITIONED)
    private String query;

    @PrimaryKeyColumn(name = "domain",ordinal = 2)
    private String domain;

    @PrimaryKeyColumn(name = "posResponse",ordinal = 1)
    private int posResponse;

    @PrimaryKeyColumn(name = "negResponse",ordinal = 0, ordering = Ordering.DESCENDING)
    private int negResponse;
}
