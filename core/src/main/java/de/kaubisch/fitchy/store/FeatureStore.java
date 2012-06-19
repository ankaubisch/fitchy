package de.kaubisch.fitchy.store;

import java.util.HashMap;
import java.util.Map;

import de.kaubisch.fitchy.Feature;
import de.kaubisch.fitchy.FeatureStatus;
import de.kaubisch.fitchy.loader.FitchyOptions;

public class FeatureStore {

	private Map<String, Feature> featureMap;
	private FitchyOptions options;
	
	
	public FeatureStore(FitchyOptions options) {
		featureMap = new HashMap<String, Feature>();
		this.options = options;
	}
	
	public void addFeature(String key) {
		featureMap.put(key, new Feature(key, options.enabled));
	}
	
	public void addFeature(Feature feature) {
		featureMap.put(feature.name, feature);
	}
	
	public boolean hasFeature(String key) {
		return !"".equals(key) && featureMap.get(key) != null;
	}
	
	public boolean featureHasStatus(String key, FeatureStatus status) {
		if(hasFeature(key)) {
			return featureMap.get(key).status == status;
		}
		
		return false;
	}
	
	public void clear() {
		featureMap.clear();
	}
}
