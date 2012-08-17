package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.FeatureChecker;
import de.kaubisch.fitchy.FeatureContext;

public class FeatureCheckerApp {

	private FeatureContext context;
	
	public FeatureCheckerApp() {
		context = FeatureContext.Builder
					.fromUrl(this.getClass().getResource("/sample_features.properties"))
					.build();
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
