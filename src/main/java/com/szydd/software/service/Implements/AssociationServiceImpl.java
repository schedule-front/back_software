package com.szydd.software.service.Implements;

import com.szydd.software.reporistory.AssociationRepository;
import com.szydd.software.domain.Association;
import com.szydd.software.domain.User;
import com.szydd.software.service.Interface.AssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("AssociationService")
public class AssociationServiceImpl implements AssociationService {
    @Autowired
    private AssociationRepository associationRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public Association addAssociation(Association association) {
        association.setAssociationId(findLargestAssociationId()+1);
        return associationRepository.insert(association);
    }

    @Override
    public String getId(Long associationId) {
        return findAssociationByAssociationId(associationId).get_id();
    }

    @Override
    public void deleteAssociation(String _id) {
        associationRepository.deleteById(_id);
    }

    @Override
    public void deleteUserByAssociationId(Long associationId) {
        associationRepository.deleteByAssociationId(associationId);
    }

    @Override
    public Association updateAssociation(Association association) {
        return associationRepository.save(association);
    }

    @Override
    public Association findAssociationById(String id) {
        Association association = associationRepository.findById(id).get();
        return association;
    }

    @Override
    public Association findAssociationByAssociationId(Long associationId) {
        return associationRepository.findByAssociationId(associationId);
    }

    @Override
    public List<Association> findAllAssociation() {
        return null;
    }
    // 得到社团详情 按页
    @Override
    public List<Association> findAllAssociation(int page, int row) throws Exception {
        List<AggregationOperation> ops = new ArrayList<>();
//        ops.add(Aggregation.match(Criteria.where("associationId").is(associationId)));
//        ops.add(Aggregation.lookup("User","members","userId","Users"));
//        ops.add(Aggregation.unwind("Users"));
//        ops.add(Aggregation.replaceRoot("Users"));
        ops.add(Aggregation.skip((long)(page - 1) * row));
        ops.add(Aggregation.limit(row));
        List<Association> results = mongoTemplate.aggregate(Aggregation.newAggregation(ops), "Association", Association.class).getMappedResults();
        return results;
    }
    // 得到社员列表
    @Override
    public List<User> queryAllUserByAssociationId(Long associationId, int page, int row) {
        List<AggregationOperation> ops = new ArrayList<>();
        ops.add(Aggregation.match(Criteria.where("associationId").is(associationId)));
        ops.add(Aggregation.lookup("User","members","userId","Users"));
        ops.add(Aggregation.unwind("Users"));
        ops.add(Aggregation.replaceRoot("Users"));
        ops.add(Aggregation.skip((long)(page - 1) * row));
        ops.add(Aggregation.limit(row));
        List<User> results = mongoTemplate.aggregate(
                Aggregation.newAggregation(ops),
                Association.collectionName,
                User.class).getMappedResults();
        return results;
    }

    @Override
    public void addMembers(Long associationId,String userId) {

        Criteria criteria = Criteria.where("associationId").is(associationId);
        Query query = new Query(criteria);
        Update update = new Update();
        update.addToSet("members",userId);

        mongoTemplate.upsert(query,update,"Association");
    }

    @Override
    public Long countAll() {
        return associationRepository.count();
    }

    @Override
    public Long countAll(Long associationId) {
        List<AggregationOperation> ops = new ArrayList<>();
        ops.add(Aggregation.match(Criteria.where("associationId").is(associationId)));
        ops.add(Aggregation.project("cnt").and(
                ArrayOperators.Size.lengthOfArray(ConditionalOperators.ifNull("members").then(Collections.emptyList())))
                .as("cnt"));

        List<Object> ans = mongoTemplate.aggregate(Aggregation.newAggregation(ops), "Association",Object.class).getMappedResults();


        return ((Integer)((LinkedHashMap)ans.get(0)).get("cnt")).longValue();
    }

    @Override
    public String findDuityByAssociationIdAndUserId(Long associationId, String userId) {
        return findAssociationByAssociationId(associationId).getDuties().get(userId);
    }

    @Override
    public void changeDutyFromTo(Long associationId, String beforeUserId, String afterUserId, String duty) {
        deleteDuty(associationId,beforeUserId, duty);
        addDuty(associationId, afterUserId,duty);
    }

    @Override
    public void addDuty(Long associationId, String userId, String duty) {
        Association association = findAssociationByAssociationId(associationId);

        // set UserIdToDuity
        Map<String,String> duites = association.getDuties();
        if (duites == null) {
            duites = new HashMap<>();
        }
        duites.put(userId,duty);

        association.setDuties(duites);

        Map<String,Set<String> > duityToUsers = association.getDuityToUsers();
        if (duityToUsers == null) {
            duityToUsers = new HashMap<>();
        }
        Set<String> duityUsers;
        duityUsers = duityToUsers.get(duty);
        if (duityUsers == null) {
            duityUsers = new TreeSet<String>();
        }
        duityUsers.add(userId);
        duityToUsers.put(duty,duityUsers);
        association.setDuityToUsers(duityToUsers);
        updateAssociation(association);
    }

    @Override
    public void deleteDuty(Long associationId, String userId ,String duty) {
        Association association = findAssociationByAssociationId(associationId);

        Map<String,String> duites = association.getDuties();
        if (duites == null) {
            duites = new HashMap<>();
        }
        duites.remove(userId);

        association.setDuties(duites);

        Map<String,Set<String> > duityToUsers = association.getDuityToUsers();
        Set<String> duityUsers = duityToUsers.get(duty);
        if (duityUsers == null) {
            duityUsers = new TreeSet<String>();
        }
        duityUsers.remove(userId);
        duityToUsers.put(duty,duityUsers);

        association.setDuityToUsers(duityToUsers);

        updateAssociation(association);
    }

    @Override
    public String getDuity(Long associationId, String userId) {
        Association assocation = findAssociationByAssociationId(associationId);
        Map<String,String> duityToUser = assocation.getDuties();
        if (duityToUser.get(userId) == null) {
            return "member";
        } else {
            return duityToUser.get(userId);
        }
    }

    @Override
    public Set<String> getUsersIdByAssociationAndDutiy(Long associationId, String dutiy) {
        Map<String,Set<String> > mp = findAssociationByAssociationId(associationId).getDuityToUsers();
        if (mp == null) return null;
        return mp.get(dutiy);
    }

    @Override
    public Long countByNameContains(String key) {
        return associationRepository.countByNameContains(key);
    }

    @Override
    public List<Association> findAllByNameContains(String name, int page, int rows) {
        Criteria criteria = Criteria.where("name").regex(name);
        Aggregation agg;
        agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
        );
        return mongoTemplate.aggregate(agg,Association.collectionName,Association.class).getMappedResults();
    }

    @Override
    public boolean deleteMember(Long associationId, String userId) throws Exception {

        Association association = findAssociationByAssociationId(associationId);
        if (association.getDuties().containsKey(userId)) {
            return false;
        }
        List<String> members = association.getMembers();
        if (members.contains(userId) == false) {
            return false;
        }


        User user = userService.findUserByUserid(userId);
        List<Long> ass = user.getAssociations();
        if (ass.contains(associationId) == false) {
            return false;
        }


        ass.remove(associationId);
        user.setAssociations(ass);
        userService.updateUser(user);

        members.remove(userId);
        association.setMembers(members);
        associationRepository.save(association);

        return true;
    }


    @Override
    public Long findLargestAssociationId() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC,"associationId"),
                Aggregation.limit(1)
        );
        AggregationResults<Association> associationAggregationResults = mongoTemplate.aggregate(agg
                ,"Association"
                ,Association.class);
        List<Association> associations = associationAggregationResults.getMappedResults();
        if (associations.size() == 0)
            return (long)1;
        else {
            return associations.get(0).getAssociationId();
        }
    }



}
