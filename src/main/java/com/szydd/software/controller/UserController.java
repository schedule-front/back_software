package com.szydd.software.controller;

//import com.google.gson.Gson;
import com.szydd.software.domain.User;

import com.szydd.software.model.ResultMap;
import com.szydd.software.service.Implements.ActivityServiceImpl;
import com.szydd.software.service.Implements.AnnouncementServiceImpl;
import com.szydd.software.service.Implements.AssociationServiceImpl;
import com.szydd.software.service.Implements.UserServiceImpl;
import com.szydd.software.util.JWTUtil;
import com.szydd.software.util.MD5Util;
import com.szydd.software.util.RedisUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;


import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/user")
@RestController
public class UserController {

    private static ResultMap resultMap = new ResultMap();

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AssociationServiceImpl associationService;

    @Autowired
    private ActivityServiceImpl activityService;

    @Autowired
    private AnnouncementServiceImpl announcementService;

    @RequestMapping(value= {"/login"},method = RequestMethod.POST)
    public ResultMap login(@RequestParam(value="userId") String uid,@RequestParam(value = "password") String password) {
        String mdpwd = MD5Util.inputPassToFormPass(password);
        User user = userService.login(uid, mdpwd);
        String realPassword = user == null ? "" : user.getPassword();
        if (user == null) {
            return resultMap.fail().code(401).message("用户名或密码错误");
        } else if (!realPassword.equals(MD5Util.inputPassToFormPass(password))) {
            return resultMap.fail().code(401).message("用户名或密码错误");
        } else {
            HashMap<String, Object> token = new HashMap<>();
            String Token = JWTUtil.createToken(uid);
            token.put("token", Token);
            Jedis jedis = RedisUtil.getJedis();
            if(jedis.set(Token, "1", "NX", "EX", 5 * 60) == null){
                RedisUtil.returnResource(jedis);
                return resultMap.fail().code(401).message("Token创建错误");
            }
            RedisUtil.returnResource(jedis);
            return resultMap.success().code(20000).data(token);
        }
    }

    @RequestMapping(value= {"/info"},method = RequestMethod.GET)
    @RequiresRoles(value={"student","president","admin"},logical = Logical.OR)
    public ResultMap info(@RequestHeader(value="X-Token") String token){
        String userId = JWTUtil.getUsername(token);
        User user = userService.findUserByUserid(userId);
        List roles = new ArrayList();
        roles.add(userService.getRole(userId));
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("presidentOf", null);
        data.put("roles",roles);
        data.put("gender",user.getGender());
        data.put("class",user.getUserClass());
        data.put("major",user.getMajor());
        data.put("email",user.geteMail());
        data.put("name",user.getName());
        data.put("userid",user.getUserId());
        data.put("phone",user.getPhone());
        List<Long> associations = user.getAssociations();
        for(Long id:associations){
            String duty = associationService.findDuityByAssociationIdAndUserId(id, user.getUserId());
            if (duty!=null && duty.equals("president")){
                data.put("presidentOf", id);
                break;
            }
        }
        data.put("associations",associations);
        return resultMap.code(20000).data(data);
    }

    @RequestMapping(value = "/find",method = RequestMethod.GET)
    @RequiresRoles(value={"student","president","admin"},logical = Logical.OR)
    public ResultMap find(@RequestParam String userId) {
        return resultMap.code(20000).data(userService.findUserByUserid(userId));
    }

    @RequestMapping(value = "/dashboard",method = RequestMethod.GET)
    public ResultMap dashboard(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("userCount", userService.countAll());
        data.put("activityCount", activityService.countAll());
        data.put("announcementCount", announcementService.countAll());
        data.put("associationCount",associationService.countAll());
        return resultMap.code(20000).data(data);
    }

//    @RequestMapping(value= {"/modifypwd"},method = RequestMethod.POST)
//    public ResultMap modifypwd(@RequestParam String userId, @RequestParam(value = "oldpwd") String oldpwd, @RequestParam(value = "newpwd") String newpwd){
////        String userId = JWTUtil.getUsername(token);
//        User user = userService.findUserByUserid(userId);
//        String mdOldPwd = MD5Util.inputPassToFormPass(oldpwd);
////        if(mdOldPwd.equals(user.getPassword())){
//            String mdNewPwd = MD5Util.inputPassToFormPass(newpwd);
//            user.setPassword(mdNewPwd);
//            userService.updateUser(user);
//            return resultMap.code(20000).message("密码修改成功");
////        }
////        return resultMap.message("密码错误").fail();
//    }

    @RequestMapping(value= {"/modifypwd"},method = RequestMethod.POST)
    public ResultMap modifypwd(@RequestHeader(value = "X-Token") String token, @RequestParam(value = "oldpwd") String oldpwd, @RequestParam(value = "newpwd") String newpwd){
        String userId = JWTUtil.getUsername(token);
        User user = userService.findUserByUserid(userId);
        String mdOldPwd = MD5Util.inputPassToFormPass(oldpwd);
        if(mdOldPwd.equals(user.getPassword())){
        String mdNewPwd = MD5Util.inputPassToFormPass(newpwd);
        user.setPassword(mdNewPwd);
        userService.updateUser(user);
        return resultMap.code(20000).message("密码修改成功");
        }
        return resultMap.message("原密码错误").fail();
    }


    @RequestMapping(value= {"/logout"},method = RequestMethod.POST)
    public ResultMap logout(@RequestHeader(value = "X-Token") String token){
        Jedis jedis = RedisUtil.getJedis();
        jedis.del(token);
        RedisUtil.returnResource(jedis);
        return resultMap.code(20000).message("success");
    }

    @RequestMapping(path = "/unauthorized/{message}")
    public ResultMap unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return resultMap.success().code(401).message(message);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @RequiresRoles(value={"student","president","admin"},logical = Logical.OR)
    public User updateUser(@Valid User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResultMap registerUser(@Valid User user) {
        user.setRole("student");
        String mdpwd = MD5Util.inputPassToFormPass(user.getPassword());
        user.setPassword(mdpwd);
        user.setAssociations(new ArrayList<>());
        userService.addUser(user);
        return resultMap.code(20000).message("注册成功");
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    @RequiresRoles(value = {"president","admin"},logical = Logical.OR)
    public User test(@RequestParam Long id) {
        String name = associationService.findAssociationByAssociationId(id).getName();
        System.out.println(name);
        return null;
    }
}
