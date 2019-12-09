package com.szydd.software.reporistory;

import com.szydd.software.domain.Account;
import com.szydd.software.domain.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("AccountRepository")
public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByAccountId(String id);
//    @Query(value = "{'associationId':?0}")
//    Page<Activity> queryAllByAssociationId (String associationId, Pageable pageable) throws Exception;
}
