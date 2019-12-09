package com.szydd.software.controller;

import com.szydd.software.domain.Announcement;
import com.szydd.software.model.ResultMap;
import com.szydd.software.service.Implements.AnnouncementServiceImpl;
import com.szydd.software.service.Interface.AnnouncementService;
import com.szydd.software.util.JWTUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RequestMapping(value = "/annoncement")
@RestController
public class AnnouncementsController {
    private static ResultMap resultMap = new ResultMap();

    @Autowired
    private AnnouncementServiceImpl announcementService;

    @RequestMapping(value= {"/add"},method = RequestMethod.POST)
    @RequiresRoles(value={"president","admin"},logical = Logical.OR)
    public ResultMap add(@RequestBody Announcement announcement){
        announcementService.addAnnouncement(announcement);
        return resultMap.code(20000).message("通知发布成功");
    }

    @RequestMapping(value= {"/getclubann"},method = RequestMethod.GET)
    public ResultMap getclubann(@RequestParam(value = "associationId") Long associationId, @RequestParam(value = "page") int page, @RequestParam(value = "limit") int limit) throws Exception {
        return resultMap.code(20000).data(announcementService.findAllAnnouncementLatest(associationId,page,limit));
    }
}
