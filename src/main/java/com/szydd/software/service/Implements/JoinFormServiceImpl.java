package com.szydd.software.service.Implements;

import com.szydd.software.domain.Award;
import com.szydd.software.domain.JoinForm;
import com.szydd.software.reporistory.JoinFormRepository;
import com.szydd.software.service.Interface.JoinFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("JoinFormService")
public class JoinFormServiceImpl implements JoinFormService {
    @Autowired
    private JoinFormRepository joinFormRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public JoinForm addJoinForm(JoinForm joinForm) {
        joinForm.setJoinFormId(findLargestJoinFormId()+1);
        joinForm.setStatus(JoinForm.statusType[0]);
        return joinFormRepository.insert(joinForm);
    }

    @Override
    public String getId(Long joinFormId) {
        JoinForm joinForm = joinFormRepository.findByJoinFormId(joinFormId);
        if (joinForm == null) return null;
        return joinFormRepository.findByJoinFormId(joinFormId).get_id();
    }

    @Override
    public void deleteJoinForm(String _id) {
        joinFormRepository.deleteById(_id);
    }

    @Override
    public void deleteJoinFormByJoinFormId(Long joinFormId) {
        joinFormRepository.delete(joinFormRepository.findByJoinFormId(joinFormId));
    }

    @Override
    public JoinForm updateJoinForm(JoinForm joinForm) {
         return joinFormRepository.save(joinForm);
    }

    @Override
    public JoinForm findJoinFormByJoinFormId(Long joinFormId) {
        return joinFormRepository.findByJoinFormId(joinFormId);
    }

    @Override
    public List<JoinForm> findAllJoinForm(Long associationId, int page, int rows) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("associationId").is(associationId)),
                Aggregation.sort(Sort.Direction.DESC,"date"),
                Aggregation.skip((long)rows*(page - 1)),
                Aggregation.limit(rows)
        );
        AggregationResults<JoinForm> joinFormAggregationResults = mongoTemplate.aggregate(agg
                ,"JoinForm"
                ,JoinForm.class);
        List<JoinForm> JoinForm = joinFormAggregationResults.getMappedResults();
        return JoinForm;
//        Pageable pages = PageRequest.of(page - 1,limit,Sort.by(Sort.Direction.DESC,"joinFormId"));
//        return joinFormRepository.queryAllByAssociationId(associationId,pages);
//        return null;
    }

    @Override
    public List<JoinForm> findAllJoinFormByAssociationIdAndStatus(Long associationId, String status, int page, int rows) {
//        Pageable pages = PageRequest.of(page - 1,limit,Sort.by(Sort.Direction.DESC,"joinFormId"));
////        return joinFormRepository.queryAllByAssociationIdAndStatus(associationId,status,pages);
//        return null;
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("associationId").is(associationId)),
                Aggregation.match(Criteria.where("status").is(status)),
                Aggregation.sort(Sort.Direction.DESC,"date"),
                Aggregation.skip((long)rows*(page - 1)),
                Aggregation.limit(rows)
        );
        AggregationResults<JoinForm> joinFormAggregationResults = mongoTemplate.aggregate(agg
                ,"JoinForm"
                ,JoinForm.class);
        List<JoinForm> JoinForm = joinFormAggregationResults.getMappedResults();
        return JoinForm;
    }

    @Override
    public Long findLargestJoinFormId() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC,"joinFormId"),
                Aggregation.limit(1)
        );
        AggregationResults<JoinForm> joinFormAggregationResults = mongoTemplate.aggregate(agg
                ,"JoinForm"
                ,JoinForm.class);
        List<JoinForm> joinForms = joinFormAggregationResults.getMappedResults();
        if (joinForms.size() == 0)
            return (long)1;
        else {
            return joinForms.get(0).getJoinFormId();
        }
    }

    @Override
    public void Approve(Long joinFormId) {
        JoinForm joinForm = findJoinFormByJoinFormId(joinFormId);
        Long associationId = joinForm.getAssociationId();
        String userId = joinForm.getUserId();


        Criteria criteria = Criteria.where("associationId").is(associationId);
        Query query = new Query(criteria);

        Update update = new Update();
        update.addToSet("members",userId);
        mongoTemplate.upsert(query,update,"Association");

        Criteria criteria1 = Criteria.where("userId").is(userId);
        Query query1 = new Query(criteria1);
        Update update1 = new Update();
        update1.addToSet("associations",associationId);
        mongoTemplate.upsert(query1,update,"User");

        joinForm.setStatus(JoinForm.statusType[1]);

        this.updateJoinForm(joinForm);
    }

    @Override
    public void DisApprove(Long joinFormId) {
        JoinForm joinForm = findJoinFormByJoinFormId(joinFormId);
        joinForm.setStatus(JoinForm.statusType[2]);

        this.updateJoinForm(joinForm);
    }

    @Override
    public JoinForm findByUserIdAndAssociationId(Long associationId, String userId) {
        return joinFormRepository.findByUserIdAndAssociationId(associationId,userId);
    }

    @Override
    public Long countByAssociationId(Long AssociationId) {
        return joinFormRepository.countByAssociationId(AssociationId);
    }

    @Override
    public Long countByAssociationIdAndStatus(Long AssociationId, String status) {
        return joinFormRepository.countByAssociationIdAndStatus(AssociationId,status);
    }
}
