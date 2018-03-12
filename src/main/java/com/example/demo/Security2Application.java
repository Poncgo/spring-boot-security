package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class Security2Application {

    @Autowired
    RequestMappingHandlerConfig requestMappingHandlerConfig;
    
	public static void main(String[] args) {
		SpringApplication.run(Security2Application.class, args);
	}
	
	@PostConstruct
    public void detectresource(){
        final RequestMappingHandlerMapping requestMappingHandlerMapping = requestMappingHandlerConfig.requestMappingHandlerMapping ();
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        Set<RequestMappingInfo> mappings = map.keySet();
        Map<String, String> reversedMap = new HashMap<String, String>();
        for(RequestMappingInfo info : mappings) {
            HandlerMethod method = map.get(info);
            String methodstr = method.toString();
            methodstr = methodstr.split("\\(")[0];

            String urlparm = info.getPatternsCondition().toString();
            String url = urlparm.substring(1, urlparm.length()-1);
            System.out.println("方法路径："+methodstr);
            System.out.println("请求相对路径："+url);


        }
    }
}
