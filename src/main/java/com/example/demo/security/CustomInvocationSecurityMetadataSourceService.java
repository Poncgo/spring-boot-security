package com.example.demo.security;

import com.example.demo.entity.Resource;
import com.example.demo.entity.ResourceRole;
import com.example.demo.entity.Role;
import com.example.demo.mapper.ResourceMapper;
import com.example.demo.mapper.ResourceRoleMapper;
import com.example.demo.mapper.RoleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;


/**
 * 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。 此类在初始化时，应该取到所有资源及其对应角色的定义。
 */
@Service
public class CustomInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private RoleMapper roleMap;

    @Autowired
    private ResourceMapper resourceMap;
    
    @Autowired
    private ResourceRoleMapper resourceroleMap;

    private static Map<String, Collection<ConfigAttribute>> resource = null;

    /*public CustomInvocationSecurityMetadataSourceService(SResourceService sres,SRoleService sR) {
        this.sResourceService = sres;
        this.sRoleService = sR;
        loadResourceDefine();
    }*/

    /**
     *      被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
     *      PostConstruct在构造函数之后执行,init()方法之前执行。
     *      此处一定要加上@PostConstruct注解
     *
     *      注意: 形成以URL为Key,权限列表为Value的Map时，注意Key和Value的对应性，
     *      避免Value的不正确对应形成重复，这样会导致没有权限的也能访问到不该访问的资源
     */
    @PostConstruct
    private void loadResourceDefine() {
        if (null == resource)
           resource =  new HashMap<String, Collection<ConfigAttribute>>();
        
        List<Resource> rslist = resourceMap.getAll();
        for (Resource rs : rslist)
        {
            
            List<ResourceRole> rrlist = resourceroleMap.fingByResourceId(rs.getId());
  
            Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
            for(ResourceRole rr : rrlist)
            {
               Role r = roleMap.selectByPrimaryKey(rr.getRoleid());
               if (null != r)
               {
                   ConfigAttribute configAttribute = new SecurityConfig(r.getRolename());
                   atts.add(configAttribute);
               }
               System.out.println("url->" + rs.getName() + " role->" + r.getRolename());
            }
            resource.put(rs.getName(), atts);   
        }
        
    }

    /**
     *  这里这样写有问题，获取的是一个空的
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<ConfigAttribute>();
    }

    /**
     *  根据URL，找到相关的权限配置。
     *  要有URL.indexof("?")这样的判断，主要是对URL你问号之前的匹配，避免对用户请求带参数的URL与数据库里面的URL不匹配，造成访问拒绝
      */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
          // object 是一个URL，被用户请求的url。
          FilterInvocation filterInvocation = (FilterInvocation) object;
            if (resource == null) {
                loadResourceDefine();
            }
            Iterator<String> iterator = resource.keySet().iterator();
            while (iterator.hasNext()) {
                String resURL = iterator.next();
                // 优化请求路径后面带参数的部分
                RequestMatcher requestMatcher = new AntPathRequestMatcher(resURL);
                if(requestMatcher.matches(filterInvocation.getHttpRequest())) {
                    return resource.get(resURL);
                }
            }
            return null;
    }
    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}
