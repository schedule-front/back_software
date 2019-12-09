package com.szydd.software.service.Implements;

import com.szydd.software.domain.Account;
import com.szydd.software.domain.Announcement;
import com.szydd.software.domain.Association;
import com.szydd.software.reporistory.ActivityRepository;
import com.szydd.software.domain.Activity;

import com.szydd.software.service.Interface.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("ActivityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    // 添加活动
    @Override
    public Activity addActivity(Activity activity) {
        activity.setActivityId(findLargestActivityId()+1);
        activity.setStatus(Activity.statusType[0]);
        return activityRepository.insert(activity);
    }




    // 得到Id
    @Override
    public String getId(Long activityId) {
        return this.findActivityByActivityid(activityId).get_id();
    }

    @Override
    public Long countAll() {
        return activityRepository.count();
    }

    // 删除活动
    @Override
    public void deleteActivity(String _id) {
        this.activityRepository.deleteById(_id);
    }

    // 删除活动
    @Override
    public void deleteUserByActivityId(Long activityId) {
        Activity activity = activityRepository.findByActivityId(activityId);
        if (activity != null) {
            this.deleteActivity(activity.get_id());
        }
    }

    // 修改活动
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Activity updateActivity(Activity activity) {
        Activity oldUser = this.findActivityById(activity.get_id());
        if (oldUser != null) {
            return activityRepository.save(activity);
        } else {
            return null;
        }
    }

    // 查询活动
    @Override
    public Activity findActivityById(String id) {

        return activityRepository.findById(id).get();
    }

    // 查询活动
    @Override
    public Activity findActivityByActivityid(Long activityId) {
        return activityRepository.findByActivityId(activityId);
    }

    // 查询所有活动
    @Override
    public List<Activity> findAllActivity() {
        return activityRepository.findAll();
    }

    @Override
    public List<Activity> findAllActivity(int page, int rows) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC,"beginDate"),
                Aggregation.skip((long)rows*(page - 1)),
                Aggregation.limit(rows)
        );
        AggregationResults<Activity> activityAggregationResults = mongoTemplate.aggregate(agg
                ,Activity.collectionName
                ,Activity.class);
        List<Activity> activitys = activityAggregationResults.getMappedResults();
        return activitys;
    }


    @Override
    public Long findLargestActivityId() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC,"activityId"),
                Aggregation.limit(1)
        );
        AggregationResults<Activity> activityAggregationResults = mongoTemplate.aggregate(
                agg,
                Activity.collectionName,
                Activity.class);
        List<Activity> activities = activityAggregationResults.getMappedResults();
        if (activities.size() == 0)
            return (long)1;
        else {
            return activities.get(0).getActivityId();
        }
    }
    // 按页查询社团活动
    @Override
    public List<Activity> findAllActivity(Long associationId, int page, int rows) throws Exception {
        Criteria criteria = new Criteria().where("associationId").is(associationId);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.DESC,"beginDate"),
                Aggregation.skip((long)rows*(page - 1)),
                Aggregation.limit(rows)
        );
        AggregationResults<Activity> activityAggregationResults = mongoTemplate.aggregate(agg
                ,Activity.collectionName
                ,Activity.class);
        List<Activity> activitys = activityAggregationResults.getMappedResults();
        return activitys;
//        Sort.Order orderDate = new Sort.Order(Sort.Direction.DESC, "date");
//        List<Sort.Order> list = new ArrayList<>();
//        list.add(orderDate);
//        Sort sort = Sort.by(list);
//        Pageable pageable = PageRequest.of(page - 1, rows,sort);
//        return activityRepository.queryAllByAssociationId(associationId,pageable).getContent();
    }

    @Override
    public List<Activity> findAllActivity(Long associationId, int page, int rows, String status) throws Exception {

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("associationId").is(associationId)),
                Aggregation.match(Criteria.where("status").is(status)),
                Aggregation.sort(Sort.Direction.DESC,"beginDate"),
                Aggregation.skip((long)rows*(page - 1)),
                Aggregation.limit(rows)
        );
        AggregationResults<Activity> activityAggregationResults = mongoTemplate.aggregate(agg
                ,Activity.collectionName
                ,Activity.class);
        List<Activity> activitys = activityAggregationResults.getMappedResults();
        return activitys;
    }

    // 得到对应社团活动数
    @Override
    public Long countByAssociationId(Long associationId) throws Exception {
        return activityRepository.countByAssociationId(associationId);
    }

    @Override
    public Long countByTitleContains(String name) {
        return activityRepository.countByTitleContains(name);
    }

    @Override
    public Long countByTitleContainsAndContentContains(String name) {
        return activityRepository.countByTitleContainsOrContentContains(name);
    }

    @Override
    public List<Activity> findAllByTitleContainsAndContentContains(String name, int page, int rows) {
//        Query query = new Query().addCriteria()
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().orOperator(Criteria.where("title").regex(name),Criteria.where("content").regex(name))
                        ),
                Aggregation.sort(Sort.Direction.DESC,"beginDate"),
                Aggregation.skip((long)rows*(page - 1)),
                Aggregation.limit(rows)
        );

        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }

    @Override
    public Long countByAssociationIdAndTitleContainsOrContentContains(Long associationId, String key) {
        return activityRepository.countByAssociationIdAndTitleContainsOrContentContains(associationId,key);
    }

    @Override
    public List<Activity> findAllByAssociationIdAndTitleContainsOrContentContains(Long associationId, String key, int page, int rows) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                Criteria.where("associationId").is(associationId),
                                new Criteria().orOperator(Criteria.where("title").regex(key),
                                                            Criteria.where("content").regex(key)))
                ),
                Aggregation.sort(Sort.Direction.DESC,"beginDate"),
                Aggregation.skip((long)rows*(page - 1)),
                Aggregation.limit(rows)
        );

        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }

    @Override
    public Long countByBeginDateGreaterThanAndStatus(Long nowDate, String status) {
        return activityRepository.countByBeginDateGreaterThanAndStatus(nowDate,status);
    }

    @Override
    public List<Activity> findAllByBeginDateGreaterThanAndStatus(Long nowDate, String status, int page, int rows) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("status").is(status)),
                Aggregation.sort(Sort.Direction.ASC,"beginDate"),
                Aggregation.skip((long)rows*(page - 1)),
                Aggregation.limit(rows)
        );

        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }

    @Override
    public Long countByAssociationIdAndBeginDateGreaterThanAndStatus(Long associationId, Long nowDate, String status) {
        return countByAssociationIdAndBeginDateGreaterThanAndStatus(associationId,nowDate,status);
    }

    @Override
    public List<Activity> findAllByTitleContains(String name, int page, int rows) {
        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("title").regex(name)),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }

    @Override
    public Long countByAssociationIdAndTitleContains(Long associationId, String key) {
        return activityRepository.countByAssociationIdAndTitleContains(associationId,key);
    }

    @Override
    public List<Activity> findAllByAssociationIdAndTitleContains(Long associationId, String name, int page, int rows) {
        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                Criteria.where("title").regex(name),
                                Criteria.where("associationId").is(associationId)
                                )
                ),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }



    @Override
    public List<Activity> findAllByAssociationIdStatusBeforeBeginDate(Long associationId, Long nowDate, String status,int page,int rows) {

        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                Criteria.where("associationId").is(associationId),
                                Criteria.where("status").is(status),
                                Criteria.where("beginDate").gt(nowDate))


                                ),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }

    @Override
    public Long countByEndDateIsLessThanAndStatus(Long nowDate, String status) {
        return activityRepository.countByEndDateIsLessThanAndStatus(nowDate,status);
    }

    @Override
    public List<Activity> findAllByEndDateIsLessThanAndStatus(Long nowDate, String status,int page,int rows) {
        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                Criteria.where("status").is(status),
                                Criteria.where("endDate").lt(nowDate)
                        )
                ),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }

    @Override
    public Long countByAssociationIdAndEndDateIsLessThanAndStatus(Long associationId, Long nowDate, String status) {
        return activityRepository.countByAssociationIdAndEndDateIsLessThanAndStatus(associationId,nowDate,status);
    }

    @Override
    public List<Activity> findAllAssociationIdAndEndDateIsLessThanAndStatus(Long associationId, Long nowDate, String status,int page,int rows) {
        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                Criteria.where("status").is(status),
                                Criteria.where("endDate").lt(nowDate)
                        )

                ),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }

    @Override
    public Long countByBeginDateGreaterThanAndEndDateLessThanAndStatus(Long nowDate, String status) {
        return activityRepository.countByBeginDateGreaterThanAndEndDateLessThanAndStatus(nowDate,status);
    }

    @Override
    public List<Activity> findAllByStatusBetweenBeginDateAndEndDate(Long nowDate, String status,int page,int rows) {
        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                Criteria.where("status").is(status),
                                Criteria.where("beginDate").lt(nowDate),
                                Criteria.where("endDate").gt(nowDate)
                        )
                ),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }

    @Override
    public Long countByAssociationIdAndBeginDateGreaterThanAndEndDateLessThanAndStatus(Long associationId, Long nowDate, String status) {
        return activityRepository.countByAssociationIdAndBeginDateGreaterThanAndEndDateLessThanAndStatus(associationId,nowDate,status);
    }



    @Override
    public List<Activity> findAllByAssociationIdAndStatusBetweenBeginDateAndEndDate(Long associationId, Long nowDate, String status,int page,int rows) {
        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                Criteria.where("status").is(status),
                                Criteria.where("associationId").is(associationId),
                                Criteria.where("beginDate").lt(nowDate),
                                Criteria.where("endDate").gt(nowDate)
                        )

                ),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Activity.collectionName,Activity.class).getMappedResults();
    }


}
