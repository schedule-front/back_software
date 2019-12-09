package com.szydd.software.controller;

import com.szydd.software.domain.Association;
import com.szydd.software.domain.Award;
import com.szydd.software.domain.User;
import com.szydd.software.model.ResultMap;
import com.szydd.software.service.Implements.AssociationServiceImpl;
import com.szydd.software.service.Implements.UserServiceImpl;
import com.szydd.software.service.Interface.AssociationService;
import com.szydd.software.service.Interface.UserService;
import com.szydd.software.util.JWTUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RequestMapping(value = "/club")
@RestController
public class AssosiationController {
    private static ResultMap resultMap = new ResultMap();

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AssociationServiceImpl associationService;

    @RequestMapping(value= {"/create"},method = RequestMethod.GET)
    @RequiresRoles("admin")
    public ResultMap create(@RequestBody Association ass){
        User president = userService.findUserByUserid(ass.getPresidentId());
        if(president == null){
            return resultMap.fail().message("该用户id不存在");
        }
        Map duties = new HashMap<String,String>();
        Map dtu = new HashMap<String,Set<String>>();
        duties.put(president.getUserId(),"president");
        Set set = new HashSet();
        set.add(president.getUserId());
        dtu.put("president",set);
        ass.setDuties(duties);
        ass.setDuityToUsers(dtu);
        Association nass = associationService.addAssociation(ass);
        List pass = president.getAssociations();
        pass.add(nass.getAssociationId());
        president.setAssociations(pass);
        userService.updateUser(president);
        return resultMap.code(20000).message("社团创建成功");
    }

    @RequestMapping(value= {"/changepresident"},method = RequestMethod.POST)
    @RequiresRoles("admin")
    public ResultMap changePresident(@RequestParam(value = "associationId") Long associationId, @RequestParam(value = "newPresidentId") String newpreId){
//        System.out.println(associationService.getUsersIdByAssociationAndDutiy(associationId,"president").iterator().next());
        associationService.changeDutyFromTo(associationId, associationService.getUsersIdByAssociationAndDutiy(associationId,"president").iterator().next(), newpreId, "president");
        return resultMap.code(20000).message("社长换届修改成功");
    }

    @RequestMapping(value= {"/find"},method = RequestMethod.GET)
    public ResultMap list(@RequestParam(value = "associationId") Long associationId){
        HashMap<String, Object> data = new HashMap<>();
        Association ass = associationService.findAssociationByAssociationId(associationId);
        User president = userService.findUserByUserid(ass.getDuityToUsers().get("president").iterator().next());
        data.put("association", ass);
        data.put("president", president);
        return resultMap.code(20000).data(data);
    }

    @RequestMapping(value= {"/findmyclub"},method = RequestMethod.GET)
    @RequiresRoles(value={"student","president","admin"},logical = Logical.OR)
    public ResultMap list(@RequestParam(value = "userId") String userId){
        List<Long> associations = userService.findUserByUserid(userId).getAssociations();
        List<Association> allAss = new ArrayList<>();
        for(Long id : associations){
            allAss.add(associationService.findAssociationByAssociationId(id));
        }
        return resultMap.code(20000).data(allAss);
    }

    @RequestMapping(value= {"/list"},method = RequestMethod.GET)
    public ResultMap list(@RequestParam(value="page") int page, @RequestParam(value="limit") int limit) throws Exception {
        /*
        * 1.获取社团total
        * 2.List allClubs = getAllClubs();
        * 3.items = allClubs.subList((page-1)*limit, page*limit);
        * return resultMap.code(20000).data({"items": items, "total": total});
        */
        HashMap<String, Object> data = new HashMap<>();
        data.put("clubs", associationService.findAllAssociation(page,limit));
        data.put("total", associationService.countAll());
        return resultMap.code(20000).data(data);
    }

    @RequestMapping(value= {"/memberlist"},method = RequestMethod.GET)
    @RequiresRoles(value={"student","president","admin"},logical = Logical.OR)
    public ResultMap memberlist(@RequestParam(value = "associationId") Long associationId){
        List members = new ArrayList<>();
        HashMap<String, Object> tmp = new HashMap<>();
        Association ass = associationService.findAssociationByAssociationId(associationId);
        for(String id : ass.getMembers()){
            HashMap<String, Object> data = new HashMap<>();
            User user = userService.findUserByUserid(id);
            data.put("gender",user.getGender());
            data.put("class",user.getUserClass());
            data.put("major",user.getMajor());
            data.put("email",user.geteMail());
            data.put("name",user.getName());
            data.put("userid",user.getUserId());
            data.put("phone",user.getPhone());
            if (ass.getDuties().get(user.getUserId())!=null) {
                if(ass.getDuties().get(user.getUserId()).equals("president")){
                    data.put("duty", "president");
                    tmp.put("president",data);
                }
            } else {
                data.put("duty", "member");
                members.add(data);
            }
        }
        tmp.put("member",members);
        return resultMap.code(20000).data(tmp);
    }

    @RequestMapping(value= {"/deletemember"},method = RequestMethod.POST)
    @RequiresRoles(value={"president","admin"},logical = Logical.OR)
    public ResultMap deleteMember(@RequestParam Long associationId, @RequestParam String userId) throws Exception {
        if(associationService.deleteMember(associationId, userId)){
            return resultMap.success().code(20000).message("删除成员成功");
        }else{
            return resultMap.fail().message("不能删除社长");
        }
    }

}
