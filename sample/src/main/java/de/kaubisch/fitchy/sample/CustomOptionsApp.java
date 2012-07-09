package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.FeatureStatus;
import de.kaubisch.fitchy.Fitchy;
import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.options.FitchConfig;
import de.kaubisch.fitchy.store.FeatureContext;

public class CustomOptionsApp {

    public enum CustomFeatureStatus implements FeatureStatus {
        ENABLED("an", true),
        HIDDEN("hidden"),
        DISABLED("aus", false);

        private String systemName;
        private boolean enabled, disabled;

        private CustomFeatureStatus(String systemName) {
            this(systemName, false);
            this.disabled = false;
        }

        private CustomFeatureStatus(String systemName, boolean status) {
            this.systemName = systemName;
            this.enabled    = status;
            this.disabled   = !status;
        }

        public String getSystemName() {
            return systemName;
        }

        public boolean isEnabledStatus() {
            return enabled;
        }

        public boolean isDisabledStatus() {
            return disabled;
        }
    }

	public static interface DummyInterface {
		void doMethod();
	}

	private class DummyImplementation implements DummyInterface {

		@FeatureSwitch(value="simple_feature_de", status = "hidden")
		public void doMethod() {
			System.out.println("This app has feature 'simple_feature_de'");
		}
		
	}
	
	private DummyInterface sample;
	
	public CustomOptionsApp() {
		FitchConfig options = FitchConfig.newOption(CustomFeatureStatus.class);
        Fitchy.setConfig(options);

		FeatureContext context = Fitchy.loadStoreFromResource("/sample_features_de.properties");
		
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
