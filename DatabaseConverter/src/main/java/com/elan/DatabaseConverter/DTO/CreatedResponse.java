package com.elan.DatabaseConverter.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreatedResponse {
    Integer FailureCount;
    Integer TotalCount;
    LocalDateTime TimeStamp;
    HashMap<String,String> failureDetails;
    String ProcessTime;
}
