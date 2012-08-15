package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.ContextBuilder;
import de.kaubisch.fitchy.Fitchy;
import de.kaubisch.fitchy.FitchyConfig;
import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.FeatureContext;

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
		FitchyConfig options = FitchyConfig.getFromPropertiesStream(CustomOptionsApp.class.getResourceAsStream("/custom-fitchy-configuration.properties"));
        Fitchy.setConfig(options);

		FeatureContext context = new ContextBuilder(options).createFromUrl(CustomOptionsApp.class.getResource("/sample_features_de.properties"));
		
		sample = Fitchy.observe(new DummyImplementation(), context);
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
