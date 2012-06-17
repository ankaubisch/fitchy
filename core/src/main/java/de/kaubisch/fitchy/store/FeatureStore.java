package de.kaubisch.fitchy.store;

import java.util.HashMap;
import java.util.Map;

import de.kaubisch.fitchy.Feature;
import de.kaubisch.fitchy.annotation.FeatureSwitch;

public class FeatureStore {

	private Map<String, Feature> featureMap;
	
	public FeatureStore() {
		featureMap = new HashMap<String, Feature>();
	}
	
	public void addFeature(String key) {
		featureMap.put(key, new Feature(key, true));
	}
	
	public boolean isFeatureEnabled(String key) {
		return !"".equals(key) && featureMap.get(key) != null;
	}
	
	public boolean hasFeature(FeatureSwitch annotation) {
		return isFeatureEnabled(annotation.value());
	}
	
	public void clear() {
		featureMap.clear();
	}
}
