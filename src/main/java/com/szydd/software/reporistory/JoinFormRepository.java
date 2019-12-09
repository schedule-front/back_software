package com.szydd.software.reporistory;

import com.szydd.software.domain.Activity;
import com.szydd.software.domain.JoinForm;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface JoinFormRepository extends MongoRepository<JoinForm,String> {
//    @Query(value="{'associationId':?0}")
//    public List<JoinForm> queryAllByAssociationId(Long associationId, Pageable pageable);
//    @Query(value="{'$and':[{'associationId':?0},{'status':?1}]}")
//    public List<JoinForm> queryAllByAssociationIdAndStatus(Long associationId, String status, Pageable pageable);
    @Query(value="{'joinFormId':?0}")
    JoinForm findByJoinFormId(Long joinFormId);
    @Query(value="{'$and':[{'associationId':?0},{'userId':?1}]}")
    JoinForm findByUserIdAndAssociationId(Long AssociationId,String userId);
    Long countByAssociationId(Long AssociationId);
    Long countByAssociationIdAndStatus(Long AssociationId,String status);
}
