package com.example.demo.security;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserRoleMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMap;
    
    @Autowired
    private UserRoleMapper userRoleMap;
    
    @Autowired
    private RoleMapper roleMap;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //SysUser对应数据库中的用户表，是最终存储用户和密码的表，可自定义
        //本例使用SysUser中的name作为用户名:
        User user = userMap.findByName(username);
        if (null == username)
        {
            throw new UsernameNotFoundException("User" + username + "not found");
        }
        
        List<UserRole> urmaps = userRoleMap.findByUserId(user.getId());
        if (urmaps.isEmpty())
        {
            throw new UsernameNotFoundException("User" + username + " user role map not found");
        }
        
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole ur : urmaps)
        {
           Role r = roleMap.selectByPrimaryKey(ur.getRoleId());
           if (null != r)
           {
               SimpleGrantedAuthority authority = new SimpleGrantedAuthority(r.getRolename());
               authorities.add(authority);
           }
        }
        
        MyUserDetails userDetails = new MyUserDetails();
        userDetails.setPassword(user.getPasswd());
        userDetails.setUsername(username);
        userDetails.setAuthorities(authorities);
        
        return userDetails;
    }

}
