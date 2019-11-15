package com.stackroute.analyticsservice.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryResultResponseRepository extends CassandraRepository<QueryResultResponse, QueryResultResponseKey> {
    @Query(allowFiltering = true)
    List<QueryResultResponse> findByKeyDomain(final String domain);
    Boolean existsByKeyQuery(final String query);
    QueryResultResponse findByKeyQuery(final String query);
}
