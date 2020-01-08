package com.pivotal.storm.spring.bolt;

import org.apache.storm.topology.IRichBolt;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    public static void main(String[] args) {
        IRichBolt target = new StubBolt();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // context.getEnvironment().setActiveProfiles( "myProfile" );
        context.scan(target.getClass().getPackageName());
        context.refresh();

        context.getAutowireCapableBeanFactory().autowireBean(target);

        System.out.println("donE");
    }
}
