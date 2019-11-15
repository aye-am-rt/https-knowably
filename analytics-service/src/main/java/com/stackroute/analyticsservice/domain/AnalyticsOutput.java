package com.stackroute.analyticsservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsOutput {
    private int movPosResp;
    private int movNegResp;
    private int medPosResp;
    private int medNegResp;
    private double movAcc;
    private double medAcc;
}
