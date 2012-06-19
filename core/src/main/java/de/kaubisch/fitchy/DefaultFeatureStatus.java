package de.kaubisch.fitchy;

public enum DefaultFeatureStatus implements FeatureStatus {
	ON("on", true),
	OFF("off", false);

	private boolean enabled, disabled;
	private String systemName;
	
	private DefaultFeatureStatus(String systemName, Boolean status) {
		this.systemName = systemName;
		this.enabled = status != null && status;
		this.disabled = status != null && !status;
	}
	
	@Override
	public String getSystemName() {
		return systemName;
	}

	@Override
	public boolean isEnabledStatus() {
		return enabled;
	}

	@Override
	public boolean isDisabledStatus() {
		return disabled;
	}

}
