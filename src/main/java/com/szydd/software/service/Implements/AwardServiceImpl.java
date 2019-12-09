package com.szydd.software.service.Implements;

import com.szydd.software.domain.Activity;
import com.szydd.software.domain.Award;
import com.szydd.software.reporistory.AwardRepository;
import com.szydd.software.service.Interface.AwaradService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("AwardService")
public class AwardServiceImpl implements AwaradService {
    @Autowired
    private AwardRepository awardRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public Award addAward(Award award) {
        award.setAwardId(findLargestAwardId()+1);
        return awardRepository.insert(award);
    }

    @Override
    public void deleteByAwardId(Long awardId) {
        awardRepository.deleteByAwardId(awardId);
    }

    @Override
    public Award findByAwardId(Long awardId) {
        return awardRepository.findByAwardId(awardId);
    }

    @Override
    public List<Award> findAllByAssociationId(Long associationId) {
        return awardRepository.findAllByAssociationId(associationId);
    }
    @Override
    public Long findLargestAwardId() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC,"awardId"),
                Aggregation.limit(1)
        );
        AggregationResults<Award> awardAggregationResults = mongoTemplate.aggregate(agg
                ,"Award"
                ,Award.class);
        List<Award> awards = awardAggregationResults.getMappedResults();
        if (awards.size() == 0)
            return (long)1;
        else {
            return awards.get(0).getAwardId();
        }
    }

    @Override
    public List<Award> findAllByAssociationId(Long associationId, int page, int row) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("associationId").is(associationId)),
                Aggregation.sort(Sort.Direction.DESC,"date"),
                Aggregation.skip((long)row*(page - 1)),
                Aggregation.limit(row)
        );
        AggregationResults<Award> awardAggregationResults = mongoTemplate.aggregate(agg
                ,"Award"
                ,Award.class);
        List<Award> awards = awardAggregationResults.getMappedResults();
        return awards;
    }

    @Override
    public Long countByTitleContainsOrContentContains(String key) {
        return awardRepository.countByTitleContainsOrContentContains(key);
    }

    @Override
    public List<Award> findAllByTitleOrContentContains(String name, int page, int rows) {
        Criteria criteria = Criteria.where("title").regex(name);
        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg, Award.collectionName,Award.class).getMappedResults();
    }

    @Override
    public List<Award> findAllByTitleOrContentContains(Long associationId, String name, int page, int rows) {
        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().orOperator(
                                Criteria.where("title").regex(name),
                                Criteria.where("content").regex(name)
                        )
                ),
                Aggregation.match(Criteria.where("associationId").is(associationId)),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg, Award.collectionName,Award.class).getMappedResults();
    }

    @Override
    public Long countByAssociationIdAndTitleContainsOrContentContains(Long associationId, String key) {
        return awardRepository.countByAssociationIdAndTitleContainsOrContentContains(associationId,key);
    }
}
