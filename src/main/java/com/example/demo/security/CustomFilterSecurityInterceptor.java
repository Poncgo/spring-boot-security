package com.example.demo.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

@Service
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

   
    @Autowired
    CustomInvocationSecurityMetadataSourceService cisms;
    
    @Autowired
    public void setCustomAccessDecisionManager(CustomAccessDecisionManager cadm) {
        super.setAccessDecisionManager(cadm);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
            FilterInvocation fi = new FilterInvocation(request, response, chain);
            invoke(fi);
        
    }

    
    protected void invoke(FilterInvocation fi) throws IOException, ServletException
    {
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try
        {
            fi.getChain().doFilter(fi.getHttpRequest(), fi.getHttpResponse());
        }
        finally {
            super.afterInvocation(token, null);
        }
    }
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Class<?> getSecureObjectClass() {
        // TODO Auto-generated method stub
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        // TODO Auto-generated method stub
        return cisms;
    }

}
