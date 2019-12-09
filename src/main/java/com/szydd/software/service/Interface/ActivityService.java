package com.szydd.software.service.Interface;

import com.szydd.software.domain.Activity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActivityService {

    Activity addActivity(Activity activity);
    Long findLargestActivityId();
    String getId(Long activityId);
    Long countAll();

    void deleteActivity(String _id);
    void deleteUserByActivityId(Long activityId);

    Activity updateActivity(Activity activity);

    Activity findActivityById(String id);
    Activity findActivityByActivityid(Long activityId);

    List<Activity> findAllActivity();
    List<Activity> findAllActivity( int page, int row);
    List<Activity> findAllActivity(Long associationId, int page, int row) throws Exception;
    List<Activity> findAllActivity(Long associationId, int page, int row, String status) throws Exception;

    Long countByAssociationId (Long associationId) throws Exception;
    Long countByTitleContains(String name);
    public List<Activity> findAllByTitleContains(String key, int page, int rows);

    Long countByAssociationIdAndTitleContains(Long associationId,String key);
    public List<Activity> findAllByAssociationIdAndTitleContains(Long associationId, String key, int page, int rows);

    Long countByTitleContainsAndContentContains(String name);
    List<Activity> findAllByTitleContainsAndContentContains(String name,int page,int rows);

    Long countByAssociationIdAndTitleContainsOrContentContains(Long associationId,String key);
    List<Activity> findAllByAssociationIdAndTitleContainsOrContentContains(Long associationId,String key,int page,int rows);

    Long countByBeginDateGreaterThanAndStatus(Long nowDate, String status);
    List<Activity> findAllByBeginDateGreaterThanAndStatus(Long nowDate, String status,int page,int rows);

    Long countByAssociationIdAndBeginDateGreaterThanAndStatus(Long associationId, Long nowDate, String status);
    List<Activity> findAllByAssociationIdStatusBeforeBeginDate(Long associationId, Long nowDate, String status,int page,int rows);

    Long countByEndDateIsLessThanAndStatus(Long nowDate, String status);
    List<Activity> findAllByEndDateIsLessThanAndStatus(Long nowDate,String status,int page,int rows);

    Long countByAssociationIdAndEndDateIsLessThanAndStatus(Long associationId, Long nowDate, String status);
    List<Activity> findAllAssociationIdAndEndDateIsLessThanAndStatus(Long associationId,Long nowDate,String status,int page,int rows);

    Long countByBeginDateGreaterThanAndEndDateLessThanAndStatus(Long nowDate,String status);
    List<Activity> findAllByStatusBetweenBeginDateAndEndDate(Long nowDate,String status,int page,int rows);

    Long countByAssociationIdAndBeginDateGreaterThanAndEndDateLessThanAndStatus(Long associationId,Long nowDate,String status);
    List<Activity> findAllByAssociationIdAndStatusBetweenBeginDateAndEndDate(Long associationId,Long nowDate,String status,int page,int rows);
}
