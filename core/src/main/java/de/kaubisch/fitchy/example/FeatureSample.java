package de.kaubisch.fitchy.example;

import de.kaubisch.fitchy.FeatureProxy;
import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.loader.LoaderOption;
import de.kaubisch.fitchy.loader.ResourceFeatureLoader;
import de.kaubisch.fitchy.store.FeatureStore;

public class FeatureSample implements HelloWorld {
	
	public static void main(String[] args) {
		FeatureStore store = new FeatureStore();
		ResourceFeatureLoader loader = new ResourceFeatureLoader(store.getClass().getResourceAsStream("/helloworld_feature.properties"));
		LoaderOption option = loader.getOption();
		option.enabled = "an";
		option.disabled = "aus";
		
		loader.loadFeaturesIntoStore(store);
		
		HelloWorld sample = new FeatureSample();
		
		sample = (HelloWorld) FeatureProxy.newInstance(store, sample);
		
		sample.sayHello();
		sample.sayHello2();
		
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


