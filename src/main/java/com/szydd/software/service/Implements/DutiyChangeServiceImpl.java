package com.szydd.software.service.Implements;

import com.szydd.software.domain.DutiyChange;
import com.szydd.software.reporistory.DutiyChangeRepository;
import com.szydd.software.service.Interface.DutiyChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DutiyChangeService")
public class DutiyChangeServiceImpl implements DutiyChangeService {
    @Autowired
    private DutiyChangeRepository dutiyChangeRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public DutiyChange addDutiyChange(DutiyChange dutiyChange) {
        dutiyChange.setDutiyChangeId(findLargestDutiyChangeId()+1);
        return dutiyChangeRepository.insert(dutiyChange);
    }

    @Override
    public void deleteByDutiyChangeId(Long dutiyChangeId) {
        dutiyChangeRepository.deleteByDutiyChangeId(dutiyChangeId);
    }

    @Override
    public DutiyChange findByDutiyChangeId(Long dutiyChangeId) {
        return dutiyChangeRepository.findByDutiyChangeId(dutiyChangeId);
    }

    @Override
    public Long findLargestDutiyChangeId() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC,"dutiyChangeId"),
                Aggregation.limit(1)
        );
        AggregationResults<DutiyChange> dutiyChangeAggregationResults = mongoTemplate.aggregate(agg
                ,"DutiyChange"
                ,DutiyChange.class);
        List<DutiyChange> dutiyChanges = dutiyChangeAggregationResults.getMappedResults();
        if (dutiyChanges.size() == 0)
            return (long)1;
        else {
            return dutiyChanges.get(0).getDutiyChangeId();
        }
    }

    @Override
    public List<DutiyChange> findAllByPages(int page, int row) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.skip((long)page*row),
                Aggregation.limit(row)
        );
        AggregationResults<DutiyChange> dutiyChangeAggregationResults = mongoTemplate.aggregate(agg
                ,"DutiyChange"
                ,DutiyChange.class);
        List<DutiyChange> dutiyChanges = dutiyChangeAggregationResults.getMappedResults();
        return dutiyChanges;
    }

    @Override
    public List<DutiyChange> findAllByPages(String status, int page, int row) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("status").is(status)),
                Aggregation.skip((long)page*row),
                Aggregation.limit(row)
        );
        AggregationResults<DutiyChange> dutiyChangeAggregationResults = mongoTemplate.aggregate(agg
                ,DutiyChange.collectionName
                ,DutiyChange.class);
        List<DutiyChange> dutiyChanges = dutiyChangeAggregationResults.getMappedResults();
        return dutiyChanges;
    }
}
