package com.elan.DynamicDBConvert.Repository;

import com.elan.DynamicDBConvert.Entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Records,String> {
}
