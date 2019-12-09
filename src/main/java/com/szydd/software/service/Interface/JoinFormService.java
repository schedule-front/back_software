package com.szydd.software.service.Interface;

import com.szydd.software.domain.Association;
import com.szydd.software.domain.JoinForm;
import com.szydd.software.domain.User;

import java.util.List;

public interface JoinFormService {
    JoinForm addJoinForm(JoinForm joinForm);
    String getId(Long joinFormId);
    void deleteJoinForm(String _id);
    void deleteJoinFormByJoinFormId(Long joinFormId);

    JoinForm updateJoinForm(JoinForm joinForm);

//    JoinForm findJoinFormById(String id);
    JoinForm findJoinFormByJoinFormId(Long joinFormId);
//    List<User> findAllUser();
//    User login(String userId,String password);
//    List<User> findAll(int page, int rows) throws Exception;
    //    List<Activity> findAllActivity(String userId);
    public List<JoinForm> findAllJoinForm(Long associationId, int page, int limit);
    public List<JoinForm> findAllJoinFormByAssociationIdAndStatus(Long associationId,String status, int page, int limit);
    Long findLargestJoinFormId();
    // 批准加入接口
    void Approve(Long joinFormId);
    // 不批准
    void DisApprove(Long joinFormId);
    JoinForm findByUserIdAndAssociationId(Long AssociationId,String userId);
    Long countByAssociationId(Long AssociationId);
    Long countByAssociationIdAndStatus(Long AssociationId,String status);
}
