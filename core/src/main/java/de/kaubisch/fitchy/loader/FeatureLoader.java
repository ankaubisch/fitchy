package de.kaubisch.fitchy.loader;

import java.util.List;

import de.kaubisch.fitchy.Feature;
import de.kaubisch.fitchy.Fitchy;
import de.kaubisch.fitchy.options.FitchyOptions;
import de.kaubisch.fitchy.store.FeatureStore;

public abstract class FeatureLoader {
	
	protected final FitchyOptions option;
	
	public FeatureLoader() {
		this.option = Fitchy.getOptions();
	}
	
	public FitchyOptions getOption() {
		return option;
	}
	
	public void loadFeaturesIntoStore(FeatureStore store) {
		store.clear();
		for(Feature feature : getFeatureList()) {
			store.addFeature(feature);
		}
	}

	protected abstract List<Feature> getFeatureList();
}
