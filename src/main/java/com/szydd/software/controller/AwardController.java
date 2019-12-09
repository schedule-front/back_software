package com.szydd.software.controller;

import com.szydd.software.domain.Award;
import com.szydd.software.model.ResultMap;
import com.szydd.software.service.Implements.AwardServiceImpl;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping(value = "/award")
@RestController
public class AwardController {
    private static ResultMap resultMap = new ResultMap();

    @Autowired
    private AwardServiceImpl awardService;

    @RequestMapping(value= {"/get"},method = RequestMethod.GET)
    public ResultMap get(@RequestParam(value = "associationId") Long associationId){
        List awards = awardService.findAllByAssociationId(associationId);
        if(awards.size()>4)
            return resultMap.code(20000).data(awards.subList(0,4));
        else
            return resultMap.code(20000).data(awards);
    }

    @RequestMapping(value= {"/add"},method = RequestMethod.POST)
    @RequiresRoles(value={"president","admin"},logical = Logical.OR)
    public ResultMap add(@RequestBody Award award){
        //TODO
        awardService.addAward(award);
        return resultMap.code(20000).message("奖项添加成功");
    }

    @RequestMapping(value= {"/delete"},method = RequestMethod.GET)
    @RequiresRoles(value={"president","admin"},logical = Logical.OR)
    public ResultMap update(@RequestParam Award award){
        //TODO
        awardService.deleteByAwardId(award.getAwardId());
        return resultMap.code(20000).message("奖项删除成功");
    }

}
