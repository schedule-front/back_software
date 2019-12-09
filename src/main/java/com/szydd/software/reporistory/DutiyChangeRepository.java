package com.szydd.software.reporistory;

import com.szydd.software.domain.DutiyChange;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DutiyChangeRepository extends MongoRepository<DutiyChange, String> {
    @Query(value="{'dutiyChangeId':?0}")
    DutiyChange findByDutiyChangeId(Long dutiyChangeId);
    void deleteByDutiyChangeId(Long dutiyChangeId);
}
