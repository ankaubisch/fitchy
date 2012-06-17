package de.kaubisch.fitchy.loader;

public class LoaderOption {
	public String enabled;
	public String disabled;
	
	public static LoaderOption newOption(String enabled, String disabled) {
		LoaderOption option = new LoaderOption();
		option.enabled = enabled;
		option.disabled = disabled;
		return option;
	}
}
