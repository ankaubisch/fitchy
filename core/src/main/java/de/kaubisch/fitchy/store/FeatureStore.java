package de.kaubisch.fitchy.store;

import java.util.HashMap;
import java.util.Map;

import de.kaubisch.fitchy.Feature;

public class FeatureStore {

	private Map<String, Feature> featureMap;
	
	public FeatureStore() {
		featureMap = new HashMap<String, Feature>();
	}
	
	public void addFeature(String key) {
		featureMap.put(key, new Feature(key, true));
	}
	
	public boolean hasFeature(String key) {
		return !"".equals(key) && featureMap.get(key) != null;
	}
	
	public void clear() {
		featureMap.clear();
	}
}
