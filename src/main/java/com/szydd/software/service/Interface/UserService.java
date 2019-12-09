package com.szydd.software.service.Interface;

import com.szydd.software.domain.Activity;
import com.szydd.software.domain.Association;
import com.szydd.software.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface UserService {
    User addUser(User user);
    String getId(String userId);
    void deleteUser(String _id);
    void deleteUserByUserid(String userId);
    Long countAll();
    User updateUser(User user);

    User findUserById(String id);
    User findUserByUserid(String userId);
    List<User> findAllUser();
    User login(String userId,String password);
    List<User> findAll(int page, int rows) throws Exception;
//    List<Activity> findAllActivity(String userId);
    public List<Association> findAllAssociation(String userId,int page,int limit);
    String getRole(String userId);
    List<User> findAllByNameContains(String name, int page, int rows);
}
