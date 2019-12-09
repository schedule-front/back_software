package com.szydd.software.reporistory;

import com.szydd.software.domain.Activity;
import com.szydd.software.domain.Association;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ActivityRepository")
public interface ActivityRepository extends MongoRepository<Activity, String> {
//    @Query(value = "{'associationId':?0}")
//    Page<Activity> queryAllByAssociationId (Long associationId,Pageable pageable) throws Exception;



    Activity findByActivityId(Long activityId);

    @Query(value = "{'associationId':?0}")
    Long countByAssociationId (Long associationId) throws Exception;

    @Query(value = "{'$and':[{'title':{'$regex',?0}}]")
    Long countByTitleContains(String title);

    @Query(value = "{'$and':[{'associationId':?0},{'title':{'$regex',?1}}]")
    Long countByAssociationIdAndTitleContains(Long associationId,String key);

    @Query(value = "{'$and':[{'title':{'$regex',?0}},{'content':{'$regex',?0}}]")
    Long countByTitleContainsOrContentContains(String key);
    @Query(value = "{'$and':[{'associationId':?0},,{'$or':[{'title':{'$regex',?1}},{'content':{'$regex',?1}]}")
    Long countByAssociationIdAndTitleContainsOrContentContains(Long associationId,String key);

    @Query(value = "{'$and':[{'beginDate':{'$gt':?0}},{'status':?1}]")
    Long countByBeginDateGreaterThanAndStatus(Long nowDate, String status);
    @Query(value = "{'$and':[{'associationId':?0},{'beginDate':{'$gt':?1}},{'status':?2}]")
    Long countByAssociationIdAndBeginDateGreaterThanAndStatus(Long associationId, Long nowDate, String status);
    @Query(value = "{'$and':[{'endDate':{'$lt':?0}},{'status':?1}]")
    Long countByEndDateIsLessThanAndStatus(Long nowDate, String status);
    @Query(value = "{'$and':[{'associationId':?0},{'endDate':{'$lt':?1}},{'status':?2}]")
    Long countByAssociationIdAndEndDateIsLessThanAndStatus(Long associationId, Long nowDate, String status);
    @Query(value = "{'$and':[{'beginDate':{'$lt':?0}},{'endDate':{'$gt':?0}},{'status':?2}]")
    Long countByBeginDateGreaterThanAndEndDateLessThanAndStatus(Long nowDate,String status);
    @Query(value = "{'$and':[{'associationId':?0},{'beginDate':{'$lt':?1}},{'endDate':{'$gt':?1}},{'status':?2}]")
    Long countByAssociationIdAndBeginDateGreaterThanAndEndDateLessThanAndStatus(Long associationId,Long nowDate,String status);
}
