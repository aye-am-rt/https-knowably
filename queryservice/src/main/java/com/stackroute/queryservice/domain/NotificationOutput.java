package com.stackroute.queryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationOutput {
    private String status;
    private String query;
    private String[] result;
    private String[] suggestions;
}
