package com.szydd.software.reporistory;

import com.szydd.software.domain.Association;
import com.szydd.software.domain.Award;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.List;

public interface AwardRepository extends MongoRepository<Award, String> {
    @Query(value = "{'awardId':?0}")
    Award findByAwardId(Long awardId);
    @Query(value = "{'associationId':?0}")
    List<Award> findAllByAssociationId(Long associationId);

    void deleteByAwardId(Long awardId);
    List<Award> findAllByTitleContains(String name);
    @Query(value = "{'$or':[{'title':{'$regex':?1}},{'content':{'$regex':?1}}]")
    Long countByTitleContainsOrContentContains(String key);
    @Query(value = "{'$and':[{'associationId':?0},{'$or':[{'title':{'$regex':?1}},{'content':{'$regex':?1}}]}]")
    Long countByAssociationIdAndTitleContainsOrContentContains(Long associationId,String key);
//    @Query(value = "{'associationId':?0}")
//    List<Award> findAllByAssociationId(Long associationId, Pageable pageable);
}
