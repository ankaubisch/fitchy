package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.ContextBuilder;
import de.kaubisch.fitchy.FeatureContext;
import de.kaubisch.fitchy.Fitchy;
import de.kaubisch.fitchy.annotation.FeatureSwitch;

/**
 * Hello world!
 *
 */
public class HelloWorldApp 
{
	private HelloWorld sample;
	
	public static interface HelloWorld {
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
		FeatureContext context = ContextBuilder
										.fromUrl(HelloWorldApp.class.getResource("/sample_features.properties"))
										.build();
		sample = Fitchy.observe(new HelloWorldSample(), context);
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


