package com.szydd.software.shiro;

import com.szydd.software.service.Implements.UserServiceImpl;
import com.szydd.software.util.JWTUtil;
import com.szydd.software.util.RedisUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description 自定义 Realm
 * @Date 2018-04-09
 * @Time 16:58
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("————身份认证方法————");
        String token = (String) authenticationToken.getCredentials();
        // 解密获得uid，用于和数据库进行对比
        String uid = JWTUtil.getUsername(token);
        if (uid == null || !JWTUtil.verify(token, uid)) {
            throw new AuthenticationException("token认证失败！");
        }
        String password = userService.findUserByUserid(uid).getPassword();
        if (password == null) {
            throw new AuthenticationException("该用户不存在！");
        }
        Jedis jedis = RedisUtil.getJedis();
        if(jedis.set(token,"1","XX", "EX", 5 * 60) == null ){
            RedisUtil.returnResource(jedis);
            System.out.println("tttttttt");
            throw new AuthenticationException("Token已过期或不正确");
        }

        RedisUtil.returnResource(jedis);
        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("————权限认证————");

        String uid = JWTUtil.getUsername(principals.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        System.out.println(uid);
        String role = userService.getRole(uid);
        System.out.println(role);
        //每个角色拥有默认的权限
//        String rolePermission = userMapper.getRolePermission(uid);
//        //每个用户可以设置新的权限
//        String permission = userMapper.getPermission(uid);
        Set<String> roleSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();
        //需要将 role, permission 封装到 Set 作为 info.setRoles(), info.setStringPermissions() 的参数
        roleSet.add(role);
        permissionSet.add("admin");
        permissionSet.add("edit");
        //设置该用户拥有的角色和权限
        info.setRoles(roleSet);
        info.setStringPermissions(permissionSet);
        return info;
    }
}
