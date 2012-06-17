package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.FeatureChecker;
import de.kaubisch.fitchy.Fitchy;
import de.kaubisch.fitchy.store.FeatureStore;

public class FeatureCheckerApp {

	private FeatureStore store;
	
	public FeatureCheckerApp() {
		store = Fitchy.loadStoreFromResource("/sample_features.properties");
	}
	
	public void execute() {
		System.out.println(new FeatureChecker<String>(store, "simple_test") {

			@Override
			public String onFeatureEnabled() {
				return "The feature 'simple test' is enabled.";
			}

			@Override
			public String onFeatureDisabled() {
				return "The feature 'simple test' is disabled.";
			}
		}.run());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FeatureCheckerApp app = new FeatureCheckerApp();
		app.execute();
	}
}