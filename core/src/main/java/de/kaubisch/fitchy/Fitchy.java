package de.kaubisch.fitchy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import de.kaubisch.fitchy.loader.FeatureLoader;
import de.kaubisch.fitchy.loader.ResourceFeatureLoader;
import de.kaubisch.fitchy.store.FeatureStore;

public final class Fitchy {

	/**
	 * package visible constructor because there is no need for an instance
	 */
	Fitchy() {
	}
	
	public static final FeatureStore loadStoreFromResource(String resourceName) {
		return loadStoreFromUrl(Fitchy.class.getResource(resourceName));
	}
	
	public static final FeatureStore loadStoreFromUrl(URL url) {
		FeatureStore store = new FeatureStore();
		if(url != null) {
			try {
				File file = new File(url.toURI());
				if(file.exists() && file.canRead()) {
					FeatureLoader loader = new ResourceFeatureLoader(new FileInputStream(file));
					loader.loadFeaturesIntoStore(store);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		return store;
	}
}
