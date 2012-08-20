package com.googlecode.fitchy.sample;

import com.googlecode.fitchy.FeatureContext;
import com.googlecode.fitchy.ProxyBuilder;
import com.googlecode.fitchy.annotation.FeatureSwitch;

/**
 * Hello world!
 *
 */
public class HelloWorldApp 
{
	private HelloWorld sample;
	
	public interface HelloWorld {
		void sayHello();
		void sayHello2();
	}
	
	private class HelloWorldSample implements HelloWorld {
		
		public HelloWorldSample() {
			super();
		}

		@FeatureSwitch("simple_test")
		public void sayHello() {
			System.out.println("Hello World!");
		}
		
		@FeatureSwitch("feature_test")
		public void sayHello2() {
			System.out.println("Hello World2!");
		}
	}
	
	
	
	public HelloWorldApp() {
		FeatureContext context = FeatureContext.Builder
										.fromUrl(HelloWorldApp.class.getResource("/sample_features.properties"))
										.build();
		sample = ProxyBuilder.fromContext(context).build(new HelloWorldSample());
	}
	
	public void testFeatures() {
		System.out.println("Testing Feature 1:");
		sample.sayHello();
		System.out.println("Testing Feature 2:");
		sample.sayHello2();
	}
	
    public static void main( String[] args )
    {
        HelloWorldApp app = new HelloWorldApp();
        app.testFeatures();
    }
}


