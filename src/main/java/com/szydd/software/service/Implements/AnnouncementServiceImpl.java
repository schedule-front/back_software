package com.szydd.software.service.Implements;


import com.szydd.software.domain.Activity;
import com.szydd.software.domain.Association;
import com.szydd.software.reporistory.AnnoucementRepository;
import com.szydd.software.domain.Announcement;
import com.szydd.software.service.Interface.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.List;
@Service("AnnoucementService")
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private AnnoucementRepository annoucementRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public Announcement addAnnouncement(Announcement announcement) {
        announcement.setAnnouncementId(findLargestAnnouncementId()+1);
        return annoucementRepository.save(announcement);
    }



    @Override
    public String getId(Long announcementId) {
        return findAnnouncementByAnnouncementid(announcementId).get_id();
    }

    @Override
    public Long countAll() {
        return annoucementRepository.count();
    }

    @Override
    public void deleteAnnouncement(String _id) {
        annoucementRepository.deleteById(_id);
    }

    @Override
    public void deleteAnnouncementByAnnouncementId(Long announcementId) {
        annoucementRepository.delete(findAnnouncementByAnnouncementid(announcementId));
    }

    @Override
    public Announcement updateAnnouncement(Announcement announcement) {
        return annoucementRepository.save(announcement);
    }

    @Override
    public Announcement findAnnouncementById(String id) {
        return annoucementRepository.findById(id).get();
    }

    @Override
    public Announcement findAnnouncementByAnnouncementid(Long announcementId) {
        return annoucementRepository.findByAnnouncementId(announcementId);
    }

    @Override
    public List<Announcement> findAllAnnouncement() {
        return null;
    }

    @Override
    public List<Announcement> findAllAnnouncement(Long announcementId, int page, int rows) throws Exception {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC,"announcementId"),
                Aggregation.skip((long)(page - 1)*rows),
                Aggregation.limit(rows)
        );
        AggregationResults<Announcement> announcementAggregation =
                mongoTemplate.aggregate(agg,
                        Announcement.collectionName,
                        Announcement.class);
        List<Announcement> announcements = announcementAggregation.getMappedResults();
        return announcements;
    }

    @Override
    public Long countByAnnouncementId(Long announcementId) throws Exception {
        return annoucementRepository.countByAnnouncementId(announcementId);
    }

    @Override
    public Long findLargestAnnouncementId() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC,"announcementId"),
                Aggregation.limit(1)
        );
        AggregationResults<Announcement> announcementAggregation =
                mongoTemplate.aggregate(
                agg,Announcement.collectionName,
                        Announcement.class);
        List<Announcement> announcements = announcementAggregation.getMappedResults();
        if (announcements.size() == 0)
            return (long)1;
        else {
            return announcements.get(0).getAnnouncementId();
        }
    }
    @Override
    public List<Announcement> findAllAnnouncementLatest(Long associationId, int page, int row) throws Exception {
        Criteria criteria = new Criteria().where("associationId").is(associationId);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.DESC,"date"),
                Aggregation.skip((long)row*(page - 1)),
                Aggregation.limit(row)
        );
        AggregationResults<Announcement> announcementAggregationResults = mongoTemplate.aggregate(agg
                ,Announcement.collectionName
                ,Announcement.class);
        List<Announcement> announcements = announcementAggregationResults.getMappedResults();
        return announcements;
    }

    @Override
    public Long countByTitleContainsOrContentContains(String key) {
        return annoucementRepository.countByTitleContainsOrContentContains(key);
    }

    @Override
    public List<Announcement> findAllByTitleOrContentContains(String name, int page, int rows) {

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().orOperator(
                                Criteria.where("title").regex(name),
                                Criteria.where("content").regex(name)
                        )
                ),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Announcement.collectionName,Announcement.class).getMappedResults();
    }

    @Override
    public List<Announcement> findAllByTitleOrContentContains(Long associationId, String name, int page, int rows) {


        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                Criteria.where("associationId").is(associationId),
                                new Criteria().orOperator(
                                        Criteria.where("title").regex(name),
                                        Criteria.where("content").regex(name)
                                )
                        )
                ),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Announcement.collectionName,Announcement.class).getMappedResults();
    }

    @Override
    public Long countByAssociationIdAndTitleContainsOrContentContains(Long associationId, String key) {
        return annoucementRepository.countByAssociationIdAndTitleContainsOrContentContains(associationId,key);
    }

    @Override
    public Long countByAssociationId(Long associationId) {
        return annoucementRepository.countByAssociationId(associationId);
    }
}
