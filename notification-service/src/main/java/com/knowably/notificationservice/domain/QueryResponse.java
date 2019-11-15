package com.knowably.notificationservice.domain;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {
    private String query;
    private String status;
    private String[] result;
    private String[] suggestions;

}

