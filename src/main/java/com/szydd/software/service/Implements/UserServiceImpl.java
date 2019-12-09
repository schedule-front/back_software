package com.szydd.software.service.Implements;

import com.szydd.software.domain.Association;
import com.szydd.software.domain.User;
import com.szydd.software.reporistory.UserRepository;
import com.szydd.software.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AssociationServiceImpl associationService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public String getId(String userId) {
        return this.findUserByUserid(userId).getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUserByUserid(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            this.deleteUser(user.getId());
        }
    }

    @Override
    public Long countAll() {
        return userRepository.count();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(User user) {
        User oldUser = this.findUserById(user.getId());
        if (oldUser != null) {
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    @Override
    public User findUserById(String id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findUserByUserid(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }



    @Override
    public User login(String  userId,String passwd) {
        User oldUser = this.findUserByUserid(userId);
        if (oldUser != null && oldUser.getPassword().equals(passwd)) {
            return oldUser;
        } else {
            return null;
        }
    }

    @Override
    public List<User> findAll(int page, int rows) throws Exception {
        PageRequest pr = PageRequest.of(page-1, rows);
        return userRepository.findAll(pr).getContent();
    }
    @Override
    public List<Association> findAllAssociation(String userId,int page,int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
               Aggregation.lookup("Association","associations","associationId","userAssociation"),
                Aggregation.project("UserAssociation"),
                Aggregation.unwind("UserAssociation"),
                Aggregation.skip((long)limit*(page-1)),
                Aggregation.limit(limit),
                Aggregation.replaceRoot("UserAssociation"));
        AggregationResults<Association> results = mongoTemplate.aggregate(aggregation,User.collectionName, Association.class);
        return results.getMappedResults();
    }

    @Override
    public String getRole(String userId) {
        User user = findUserByUserid(userId);
        if (user.getRole().equals("admin")) {
            return "admin";
        }
        for (Long associationId : user.getAssociations()) {
            Association association = associationService.findAssociationByAssociationId(associationId);
            String duty = association.getDuties().get(userId);
            if (duty!=null && duty.equals("president")) {
                return "president";
            }
        }
        return "student";
    }

    @Override
    public List<User> findAllByNameContains(String name, int page, int rows) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("name").regex(name)),
                Aggregation.skip((long)(page - 1) * rows),
                Aggregation.limit(rows)
                );

        return mongoTemplate.aggregate(aggregation,User.collectionName,User.class).getMappedResults();
    }


}
