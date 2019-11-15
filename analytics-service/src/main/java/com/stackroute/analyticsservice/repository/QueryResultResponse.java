package com.stackroute.analyticsservice.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("Response")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResultResponse {
    @PrimaryKey private QueryResultResponseKey key;
    @Column("result")
    private String result;
}
