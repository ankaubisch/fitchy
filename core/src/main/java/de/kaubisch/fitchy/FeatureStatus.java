package de.kaubisch.fitchy;

public interface FeatureStatus {
	String getSystemName();
	
	boolean isEnabledStatus();
	
	boolean isDisabledStatus();
}
