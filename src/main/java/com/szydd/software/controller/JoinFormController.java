package com.szydd.software.controller;


import com.szydd.software.domain.JoinForm;
import com.szydd.software.model.ResultMap;
import com.szydd.software.service.Implements.ActivityServiceImpl;
import com.szydd.software.service.Implements.AssociationServiceImpl;
import com.szydd.software.service.Implements.JoinFormServiceImpl;
import com.szydd.software.service.Implements.UserServiceImpl;
import com.szydd.software.service.Interface.AssociationService;
import org.apache.shiro.authz.annotation.Logical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresRoles;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RequestMapping(value = "/joinform")
@RestController
public class JoinFormController {
    private static ResultMap resultMap = new ResultMap();
//
    @Autowired
    private JoinFormServiceImpl joinFormService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private static AssociationServiceImpl associationService;


    @RequestMapping(value= {"/create"},method = RequestMethod.POST)
    @RequiresRoles(value={"student","president","admin"},logical = Logical.OR)
    public ResultMap create(@RequestBody JoinForm joinForm){
        JoinForm jt = joinFormService.findByUserIdAndAssociationId(joinForm.getAssociationId(), joinForm.getUserId());
        if(jt==null){
            joinForm.setDate(new Date().getTime());
            joinFormService.addJoinForm(joinForm);
            return resultMap.code(20000).message("申请提交成功");
        }else{
            return resultMap.fail().code(50000).message("申请审批中");
        }
    }

    @RequestMapping(value= {"/find"},method = RequestMethod.GET)
    @RequiresRoles(value={"student","president","admin"},logical = Logical.OR)
    public ResultMap find(@RequestParam(value = "userId") String userId, @RequestParam(value = "associationId") Long associationId){
        JoinForm joinForm = joinFormService.findByUserIdAndAssociationId(associationId, userId);
        return resultMap.code(20000).data(joinForm);
    }

    @RequestMapping(value= {"/get"},method = RequestMethod.GET)
    @RequiresRoles(value={"president","admin"},logical = Logical.OR)
    public ResultMap get(@RequestParam(value = "associationId") Long associationId, @RequestParam(value = "page") int page, @RequestParam(value="limit") int limit){
        List joinform = joinFormService.findAllJoinForm(associationId, page, limit);
        HashMap<String, Object> data = new HashMap<>();
        data.put("joinForms", addName(joinform));
//        data.put("total", joinFormService.countAll(associationId));
        return resultMap.code(20000).data(data).success();
    }

    private List addName(List<JoinForm> jf){
        for(JoinForm f : jf){
            f.setName(userService.findUserByUserid(f.getUserId()).getName());
        }
        return jf;
    }

    @RequestMapping(value= {"/getdetail"},method = RequestMethod.GET)
    @RequiresRoles(value={"student","president","admin"},logical = Logical.OR)
    public ResultMap getdetail(@RequestParam(value = "joinFormId") Long joinFormId){
        JoinForm joinform = joinFormService.findJoinFormByJoinFormId(joinFormId);
        return resultMap.code(20000).data(joinform);
    }

    @RequestMapping(value= {"/accept"},method = RequestMethod.POST)
    @RequiresRoles("admin")
    public ResultMap accept(@RequestParam(value = "joinFormId") Long joinFormId){
        joinFormService.Approve(joinFormId);
        return resultMap.code(20000).message("申请通过");
    }

    @RequestMapping(value= {"/reject"},method = RequestMethod.POST)
    @RequiresRoles("admin")
    public ResultMap reject(@RequestParam(value = "joinFormId") Long joinFormId){
        joinFormService.DisApprove(joinFormId);
        return resultMap.code(20000).message("已拒绝");
    }

    @RequestMapping(value= {"/test"},method = RequestMethod.GET)
    public ResultMap test(){
        System.out.println(JoinForm.statusType[0]);
        return null;
    }

}
