/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *	or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *	regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package de.kaubisch.fitchy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;

import de.kaubisch.fitchy.loader.FeatureReader;
import de.kaubisch.fitchy.options.FitchyOptions;
import de.kaubisch.fitchy.store.FeatureStore;
/**
 * Main class to enable project for fitchy and have functions to fill stores
 * and get fitchy options.
 * 
 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
 *
 */
public final class Fitchy {

	/**
	 * Holder class to provide singleton instance of {@link FitchyOptions}
	 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
	 *
	 */
	private static class Singleton {
		public static final FitchyOptions OPTIONS = FitchyOptions.getDefault();
	}
	
	/**
	 * private constructor because there is no need for an instance
	 */
	private Fitchy() {
	}
	
	/**
	 * Loads a resource from classpath with ClassLoader of this class.
	 * Always returns an instance of {@link FeatureStore} wether it can read
	 * the resource or not.
	 * 
	 * @param resourceName name of the resource in classpath
	 * @return an instance of {@link FeatureStore}
	 */
	public static final FeatureStore loadStoreFromResource(String resourceName) {
		return loadStoreFromUrl(Fitchy.class.getResource(resourceName));
	}
	
	/**
	 * Loads a resource with feature configuration from an {@link URL} and stores all
	 * of this in a new {@link FeatureStore} and returns it.
	 *  
	 * @param url an {@link URL} to a resource with feature configuration
	 * 
	 * @return a new {@link FeatureStore} with loaded {@link Feature} items
	 */
	public static final FeatureStore loadStoreFromUrl(URL url) {
		FeatureStore store = new FeatureStore(Fitchy.getOptions());
		addFeaturesFromUrl(url, store);
		return store;
	}
	
	/**
	 * Add all features from url resource to existing {@link FeatureStore}.
	 * This overrides existing {@link Feature} added to storage.
	 * 
	 * @param url an {@link URL} to a resource that contains features
	 * @param store a {@link FeatureStore} that holds all features
	 */
	public static void addFeaturesFromUrl(URL url, FeatureStore store) {
		if(url != null) {
			try {
				File file = new File(url.toURI());
				if(file.exists() && file.canRead()) {
					fillStoreWithFeatures(store, file);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * Retrieves current options and the {@link FeatureReader} class that is set to this options.
	 * Create an instance of this {@link FeatureReader} and loads all Features from File and put them
	 * into {@link FeatureStore}.
	 * 
	 * @param store an instance of {@link FeatureStore}
	 * @param file {@link File} full of features
	 * @throws FileNotFoundException is thrown if {@link File} is not found
	 */
	private static void fillStoreWithFeatures(FeatureStore store, File file)
			throws FileNotFoundException {
		FitchyOptions option = Fitchy.getOptions();
		FeatureReader reader = null;
		try {
			Constructor<? extends FeatureReader> constructor = option.readerClass.getConstructor(InputStream.class);
			reader = constructor.newInstance(new FileInputStream(file));
			
			Feature feature = null;
			while((feature = reader.read()) != null) {
				store.addFeature(feature);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Returns a singleton of {@link FitchyOptions}
	 * 
	 * @return one instance of {@link FitchyOptions}
	 */
	public static FitchyOptions getOptions() {
		return Singleton.OPTIONS;
	}
}
