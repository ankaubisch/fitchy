package de.kaubisch.fitchy;

import de.kaubisch.fitchy.store.FeatureStore;

public abstract class FeatureChecker<T> {

	private FeatureStore store;
	private String featureName;
	
	public FeatureChecker(FeatureStore storage, String featureName) {
		this.store = storage;
		this.featureName = featureName;
	}
	
	public abstract T onFeatureEnabled();
	public abstract T onFeatureDisabled();
	
	public T run() {
		T result = null;
		if(store.hasFeature(featureName)) {
			result = onFeatureEnabled();
		} else {
			result = onFeatureDisabled();
		}
		
		return result;
	}
}
