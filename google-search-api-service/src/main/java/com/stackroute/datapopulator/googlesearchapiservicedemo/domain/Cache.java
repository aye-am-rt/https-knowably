package com.stackroute.datapopulator.googlesearchapiservicedemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//Domain object for storing in cache
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cache {
    @Id
    private String userId;
    private String domain;
    private String[] concepts;
    private LocalDateTime timestamp;
}
