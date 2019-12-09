package com.szydd.software.service.Interface;

import com.szydd.software.domain.Activity;
import com.szydd.software.domain.Association;
import com.szydd.software.domain.Award;

import java.util.List;

public interface AwaradService {
    Award addAward(Award award);
    void deleteByAwardId(Long awardId);
    public Award findByAwardId(Long awardId);
    public List<Award> findAllByAssociationId(Long associationId);
    Long findLargestAwardId();
    public List<Award> findAllByAssociationId(Long associationId,int page,int row);
    Long countByTitleContainsOrContentContains(String key);
    List<Award> findAllByTitleOrContentContains(String name, int page, int rows);
    List<Award> findAllByTitleOrContentContains(Long associationId,String name, int page, int rows);
    Long countByAssociationIdAndTitleContainsOrContentContains(Long associationId,String key);
}
