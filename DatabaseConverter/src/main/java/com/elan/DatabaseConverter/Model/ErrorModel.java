package com.elan.DatabaseConverter.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorModel {
    private String Message;
    private Integer Status;
    private LocalDateTime TimeStamp;
    private String Error;
}
