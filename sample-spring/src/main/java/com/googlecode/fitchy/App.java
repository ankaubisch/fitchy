package com.googlecode.fitchy;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        
        SpringHelloWorld hw = context.getBean(SpringHelloWorld.class);
        FeatureContext featureContext = context.getBean(FeatureContext.class);
        System.out.println("feature disabled.");
        hw.hello();
        featureContext.clear();
        featureContext.addFeature("feature_test");
        System.out.println("feature enabled.");
        hw.hello();
    }
}
