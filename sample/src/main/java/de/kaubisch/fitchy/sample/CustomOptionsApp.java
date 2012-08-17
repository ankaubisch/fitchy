package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.Configuration;
import de.kaubisch.fitchy.FeatureContext;
import de.kaubisch.fitchy.ProxyBuilder;
import de.kaubisch.fitchy.annotation.FeatureSwitch;

public class CustomOptionsApp {
	public static interface DummyInterface {
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
		Configuration options = Configuration.getFromPropertiesStream(CustomOptionsApp.class.getResourceAsStream("/custom-fitchy-configuration.properties"));

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
