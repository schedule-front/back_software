package com.szydd.software.reporistory;

//import com.szydd.software.domain.Activity;
import com.szydd.software.domain.Association;
import com.szydd.software.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("AssociationRepository")
public interface AssociationRepository extends MongoRepository<Association, String> {
    public Association findByAssociationId(Long associationId);
    void deleteByAssociationId(Long associationId);
    List<Association> findAllByNameContains(String name);
    @Query(value = "{'name':{'$regex':?0}}")
    Long countByNameContains(String key);


}
