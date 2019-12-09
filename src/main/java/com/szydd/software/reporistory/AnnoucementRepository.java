package com.szydd.software.reporistory;

import com.szydd.software.domain.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository("AnnoucementRepository")
public interface AnnoucementRepository extends MongoRepository<Announcement,String> {
    Announcement findByAnnouncementId(Long announcementId);
    @Query("{'announcementId':?0}")
    Long countByAnnouncementId(Long announcementId);

//    @Query("{'associationId':?0}")
//    Page<Announcement> findAllByAssociationId(Long associationId, Pageable pageable);

    @Query("{'associationId':?0}")
    Long countByAssociationId(Long associationId);
    @Query(value = "{'$or':[{'title':{'$regex':?0}},{'content':{'$regex':?0}}]")
    Long countByTitleContainsOrContentContains(String key);
    @Query(value = "{'$and':[{'associationId':?0},{'$or':[{'title':{'$regex':?1}},{'content':{'$regex':?1}}]}]")
    Long countByAssociationIdAndTitleContainsOrContentContains(Long associationId,String key);

}

