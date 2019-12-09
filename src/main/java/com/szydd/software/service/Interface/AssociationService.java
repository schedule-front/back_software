package com.szydd.software.service.Interface;

import com.szydd.software.domain.Association;
import com.szydd.software.domain.User;

import java.util.List;
import java.util.Set;

public interface AssociationService {
    Association addAssociation(Association association);
    Long findLargestAssociationId();
    String getId(Long associationId);


    void deleteAssociation(String _id);
    void deleteUserByAssociationId(Long associationId);

    Association updateAssociation(Association association);

    Association findAssociationById(String id);
    Association findAssociationByAssociationId(Long associationId);

    List<Association> findAllAssociation();

    List<Association> findAllAssociation(int page, int row) throws Exception;

    List<User> queryAllUserByAssociationId(Long associationId, int page,int row);
    public void addMembers(Long associationId,String userId) ;
    Long countAll();
    Long countAll(Long associationId);
    String findDuityByAssociationIdAndUserId(Long associationId, String userId);
    void changeDutyFromTo(Long associationId, String beforeUserId, String afterUserId, String duty);
    void addDuty(Long associationId, String userId, String duty);
    void deleteDuty(Long associationId, String duty, String userId);
    String getDuity(Long associationId,String userId);
    Set<String> getUsersIdByAssociationAndDutiy(Long associationId, String Dutiy);
    Long countByNameContains(String key);
    List<Association> findAllByNameContains(String name, int page, int rows);

    boolean deleteMember(Long associationId, String userId) throws Exception;
}
