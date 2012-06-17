package de.kaubisch.fitchy;

import de.kaubisch.fitchy.store.FeatureStore;

public class SingleFitchy {

	private static class Singleton {
		public static final SingleFitchy INSTANCE = new SingleFitchy();
	}
	
	private FeatureStore storage;
	
	private SingleFitchy() {
		
	}
	
	public static FeatureStore getSingletonFeatureStore() {
		SingleFitchy fitchy = Singleton.INSTANCE;
		if(fitchy.storage == null) {
			fitchy.storage = new FeatureStore();
		}
		
		return fitchy.storage;
	}
}
