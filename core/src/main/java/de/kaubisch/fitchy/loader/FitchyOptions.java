package de.kaubisch.fitchy.loader;

public class FitchyOptions {
	public static final String DEFAULT_ENABLED = "on";
	public static final String DEFAULT_DISABLED = "off";
	
	public String enabled;
	public String disabled;
	
	public static FitchyOptions newOption(String enabled, String disabled) {
		FitchyOptions option = new FitchyOptions();
		option.enabled = enabled;
		option.disabled = disabled;
		return option;
	}

	public static FitchyOptions getDefault() {
		FitchyOptions options = new FitchyOptions();
		options.enabled = DEFAULT_ENABLED;
		options.disabled = DEFAULT_DISABLED;
		return options;
	}
}
