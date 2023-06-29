package com.elan.DatabaseConverter.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
public class Records{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;
    String tableName;
    Integer totalRecords;
    String progressTime;
    Integer processedRecords;
    Integer failureRecords;
}
