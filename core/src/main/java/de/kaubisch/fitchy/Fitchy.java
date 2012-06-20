package de.kaubisch.fitchy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import de.kaubisch.fitchy.loader.FeatureLoader;
import de.kaubisch.fitchy.loader.PropertyFeatureLoader;
import de.kaubisch.fitchy.options.FitchyOptions;
import de.kaubisch.fitchy.store.FeatureStore;

public final class Fitchy {

	private static class Singleton {
		public static final FitchyOptions OPTIONS = FitchyOptions.getDefault();
	}
	
	
	/**
	 * private constructor because there is no need for an instance
	 */
	private Fitchy() {
	}
	
	public static final FeatureStore loadStoreFromResource(String resourceName) {
		return loadStoreFromUrl(Fitchy.class.getResource(resourceName));
	}
	
	public static final FeatureStore loadStoreFromUrl(URL url) {
		FeatureStore store = new FeatureStore(Fitchy.getOptions());
		addFeaturesFromUrl(url, store);
		return store;
	}
	
	public static void addFeaturesFromUrl(URL url, FeatureStore store) {
		if(url != null) {
			try {
				File file = new File(url.toURI());
				if(file.exists() && file.canRead()) {
					FeatureLoader loader = new PropertyFeatureLoader(new FileInputStream(file));
					loader.loadFeaturesIntoStore(store);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static FitchyOptions getOptions() {
		return Singleton.OPTIONS;
	}
}
