package com.googlecode.fitchy.sample;

import com.googlecode.fitchy.Configuration;
import com.googlecode.fitchy.FeatureContext;
import com.googlecode.fitchy.ProxyBuilder;
import com.googlecode.fitchy.annotation.FeatureSwitch;

public class CustomOptionsApp {
	public interface DummyInterface {
        void doMethod();
	}

	private class DummyImplementation implements DummyInterface {

		public DummyImplementation() {
			super();
		}

		@FeatureSwitch(value="simple_feature_de", status = "hidden")
		public void doMethod() {
			System.out.println("This app has feature 'simple_feature_de'");
		}
		
	}
	
	private DummyInterface sample;
	
	public CustomOptionsApp() {
		Configuration options = Configuration.Builder
									.fromStream(CustomOptionsApp.class.getResourceAsStream("/custom-fitchy-configuration.properties"))
									.build();

		FeatureContext context = FeatureContext.Builder
									.fromUrl(CustomOptionsApp.class.getResource("/sample_features_de.properties"))
									.withConfig(options)
									.build();
		
		sample = ProxyBuilder.fromContext(context).build(new DummyImplementation());
	}
	
	public void callSample() {
		sample.doMethod();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CustomOptionsApp app = new CustomOptionsApp();
		app.callSample();

	}

}
