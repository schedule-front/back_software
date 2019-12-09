package com.szydd.software.reporistory;


import com.szydd.software.domain.Association;
import com.szydd.software.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("UserRepository")
public interface UserRepository extends MongoRepository<User, String> {
    public User findByUserId(String userId);
}