package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.FeatureProxy;
import de.kaubisch.fitchy.Fitchy;
import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.loader.FitchyOptions;
import de.kaubisch.fitchy.store.FeatureStore;

public class CustomOptionsApp {

	public static interface DummyInterface {
		void doMethod();
	}
	
	private class DummyImplementation implements DummyInterface {

		@Override
		@FeatureSwitch("simple_feature_de")
		public void doMethod() {
			System.out.println("This app has feature 'simple_feature_de'");
		}
		
	}
	
	private DummyInterface sample; 
	
	public CustomOptionsApp() {
		FitchyOptions options = Fitchy.getOptions();
		options.enabled = "an";
		options.disabled = "aus";
		FeatureStore store = Fitchy.loadStoreFromResource("/sample_features_de.properties");
		
		sample = FeatureProxy.newInstance(store, new DummyImplementation());
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
