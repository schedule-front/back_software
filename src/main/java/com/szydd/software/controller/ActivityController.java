package com.szydd.software.controller;

import com.szydd.software.domain.Activity;
import com.szydd.software.domain.Association;
import com.szydd.software.model.ResultMap;
import com.szydd.software.service.Implements.ActivityServiceImpl;
import com.szydd.software.service.Implements.AssociationServiceImpl;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping(value = "/activity")
@RestController
public class ActivityController {
    private static ResultMap resultMap = new ResultMap();

    @Autowired
    private ActivityServiceImpl activityService;

    @Autowired
    private AssociationServiceImpl associationService;


    private List<Activity> updateStatus(List<Activity> acts){
        List<Activity> lists = new ArrayList<>();
        for(Activity act : acts){
            String status = act.getStatus();
            if(status==null) {
                continue;
            }
            Long now = new Date().getTime();
            if(status.equals("pass")){
                Association ass = associationService.findAssociationByAssociationId(act.getAssociationId());
                act.setName(ass.getName());
                act.setLogoUrl(ass.getLogoUrl());
                if(now > act.getEndDate()){
                    act.setStatus("done");
                }else if(now >= act.getBeginDate() && now < act.getEndDate()){
                    act.setStatus("doing");
                }else if(now > act.getEndDate()){
                    act.setStatus("done");
                }
                lists.add(act);
            }
        }
        return lists;
    }

    @RequestMapping(value= {"/list"},method = RequestMethod.GET)
    public ResultMap list() {
        List<Activity> acts = activityService.findAllActivity();
        List<Activity> nacts = updateStatus(acts);
        HashMap<String,Object> dd = new HashMap<>();
        dd.put("activities", nacts);
        dd.put("total", nacts.size());
        return resultMap.code(20000).data(dd);
    }

    @RequestMapping(value= {"/asslist"},method = RequestMethod.GET)
    public ResultMap asslist(@RequestParam Long associationId){
        System.out.println(new Date().getTime());
        List<Activity> acts = activityService.findAllByAssociationIdAndStatusBetweenBeginDateAndEndDate(associationId,new Date().getTime(),"pass", 1, 20);
        System.out.println(acts);
        return resultMap.code(20000).data(acts);
    }

    @RequestMapping(value = {"/managelist"}, method = RequestMethod.GET)
    @RequiresRoles("admin")
    public ResultMap managelist(@RequestParam int page, @RequestParam int limit){
        List<Activity> list = activityService.findAllActivity(page, limit);
        for(Activity act : list){
            System.out.println(associationService.findAssociationByAssociationId(act.getAssociationId()).getName());
            act.setName(associationService.findAssociationByAssociationId(act.getAssociationId()).getName());
        }
        Long total = activityService.countAll();
        HashMap<String, Object> data = new HashMap<>();
        data.put("activities", list);
        data.put("total", total);
        return resultMap.code(20000).data(data);
    }

    @RequestMapping(value= {"/create"},method = RequestMethod.POST)
    @RequiresRoles(value={"president","admin"},logical = Logical.OR)
    public ResultMap create(@RequestBody Activity act){
//        Activity act = new Activity();
//        act.setContent(content);
//        act.setTitle(title);
//        act.setAssociationId(associationId);
//        act.setLocation(location);
//        act.setNumPeople(numPeople);
//        act.setDate(date);
        if(act.getAssociationId() == null) return resultMap.fail().message("社团id为空");
        act.setStatus("check");
        activityService.addActivity(act);
        return resultMap.code(20000).message("活动申请创建成功");
    }

    @RequestMapping(value= {"/detail"},method = RequestMethod.GET)
    public ResultMap detail(@RequestParam(value = "activityId") Long activityId){
        Activity act = activityService.findActivityByActivityid(activityId);
        Association ass = associationService.findAssociationByAssociationId(act.getAssociationId());
        act.setName(ass.getName());
        act.setLogoUrl(ass.getLogoUrl());
        return resultMap.code(20000).data(act);
    }



    @RequestMapping(value= {"/accept"},method = RequestMethod.GET)
    @RequiresRoles("admin")
    public ResultMap accept(@RequestParam Long activityId){
        Activity act = activityService.findActivityByActivityid(activityId);
        act.setStatus("pass");
        activityService.updateActivity(act);
        return resultMap.code(20000).message("活动申请已通过");
    }

    @RequestMapping(value= {"/reject"},method = RequestMethod.GET)
    @RequiresRoles("admin")
    public ResultMap reject(@RequestParam Long activityId){
        Activity act = activityService.findActivityByActivityid(activityId);
        act.setStatus("reject");
        activityService.updateActivity(act);
        return resultMap.code(20000).message("活动申请已拒绝");
    }

    @RequestMapping(value= {"/search"},method = RequestMethod.GET)
    public ResultMap search(@RequestParam String keyword){
        List<Activity> acts = activityService.findAllByTitleContainsAndContentContains(keyword, 1, 50);
        List<Activity> nacts = updateStatus(acts);
        HashMap<String,Object> dd = new HashMap<>();
        dd.put("activities", nacts);
        dd.put("total", nacts.size());
        return resultMap.code(20000).data(dd);
    }


}
