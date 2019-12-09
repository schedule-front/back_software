package com.szydd.software.service.Interface;

import com.szydd.software.domain.Activity;
import com.szydd.software.domain.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AnnouncementService {
    Announcement addAnnouncement(Announcement announcement);
    Long findLargestAnnouncementId();
    String getId(Long announcementId);
    Long countAll();

    void deleteAnnouncement(String _id);
    void deleteAnnouncementByAnnouncementId(Long announcementId);

    Announcement updateAnnouncement(Announcement announcement);

    Announcement findAnnouncementById(String id);
    Announcement findAnnouncementByAnnouncementid(Long announcementId);


    List<Announcement> findAllAnnouncement();
    List<Announcement> findAllAnnouncement(Long announcementId,int page,int row) throws Exception;

    Long countByAssociationId(Long associationId);

    Long countByAnnouncementId (Long announcementId) throws Exception;
    List<Announcement> findAllAnnouncementLatest(Long associationId,int page,int row) throws Exception;
    Long countByTitleContainsOrContentContains(String key);
    List<Announcement> findAllByTitleOrContentContains(String name,int page,int rows);
    List<Announcement> findAllByTitleOrContentContains(Long associationId,String name,int page,int rows);
    Long countByAssociationIdAndTitleContainsOrContentContains(Long associationId,String key);


}
