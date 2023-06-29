package com.elan.DynamicDBConvert.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordsResponse {
    String tableName;
    Integer totalRecords;
    String progressTime;
    Integer processedRecords;
    Integer failureRecords;
    LocalDateTime TimeStamp;
    Integer status;
}
