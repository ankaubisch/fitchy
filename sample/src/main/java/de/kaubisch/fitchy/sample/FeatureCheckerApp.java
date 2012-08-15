package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.*;

public class FeatureCheckerApp {

	private FeatureContext context;
	
	public FeatureCheckerApp() {
		context = new ContextBuilder(FitchyConfig.getDefault()).createFromUrl(this.getClass().getResource("/sample_features.properties"));
	}
	
	public void execute() {
		System.out.println(new FeatureChecker<String>(context, "simple_test") {

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
