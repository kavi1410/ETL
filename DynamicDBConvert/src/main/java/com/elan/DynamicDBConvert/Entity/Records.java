package com.elan.DynamicDBConvert.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
