package com.googlecode.fitchy.sample;

import com.googlecode.fitchy.FeatureChecker;
import com.googlecode.fitchy.FeatureContext;

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
				return "The feature 'simple test' is enabledStatus.";
			}

			@Override
			public String onFeatureDisabled() {
				return "The feature 'simple test' is disabledStatus.";
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
