package com.test.diff.services.utils;


import org.springframework.context.ApplicationContext;

/**
 * @author wl
 */
public class SpringUtil {

    private static ApplicationContext applicationContext;

    public static void setContext(ApplicationContext context){
        applicationContext = context;
    }
    public static ApplicationContext getContext(){
        return applicationContext;
    }

}
