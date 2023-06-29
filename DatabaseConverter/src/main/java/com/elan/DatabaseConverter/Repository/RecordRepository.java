package com.elan.DatabaseConverter.Repository;

import com.elan.DatabaseConverter.Entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Records,String> {
}
