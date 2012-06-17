package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.FeatureProxy;
import de.kaubisch.fitchy.Fitchy;
import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.store.FeatureStore;

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
		FeatureStore store = Fitchy.loadStoreFromResource("/helloworld_feature.properties");
//		LoaderOption option = loader.getOption();
//		option.enabled = "an";
//		option.disabled = "aus";
		sample = (HelloWorld) FeatureProxy.newInstance(store, new HelloWorldSample());
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


