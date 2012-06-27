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

import de.kaubisch.fitchy.internal.JavaProxyObserver;
import de.kaubisch.fitchy.loader.FeatureReader;
import de.kaubisch.fitchy.options.FitchyOptions;
import de.kaubisch.fitchy.store.FeatureContext;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

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
	 * Always returns an instance of {@link de.kaubisch.fitchy.store.FeatureContext} wether it can read
	 * the resource or not.
	 * 
	 * @param resourceName name of the resource in classpath
	 * @return an instance of {@link de.kaubisch.fitchy.store.FeatureContext}
	 */
	public static final FeatureContext loadStoreFromResource(String resourceName) {
		return loadStoreFromUrl(Fitchy.class.getResource(resourceName));
	}
	
	/**
	 * Loads a resource with feature configuration from an {@link URL} and stores all
	 * of this in a new {@link de.kaubisch.fitchy.store.FeatureContext} and returns it.
	 *  
	 * @param url an {@link URL} to a resource with feature configuration
	 * 
	 * @return a new {@link de.kaubisch.fitchy.store.FeatureContext} with loaded {@link Feature} items
	 */
	public static final FeatureContext loadStoreFromUrl(URL url) {
		FeatureContext context = new FeatureContext(Fitchy.getOptions());
		addFeaturesFromUrl(url, context);
		return context;
	}
	
	/**
	 * Add all features from url resource to existing {@link de.kaubisch.fitchy.store.FeatureContext}.
	 * This overrides existing {@link Feature} added to storage.
	 * 
	 * @param url an {@link URL} to a resource that contains features
	 * @param context a {@link de.kaubisch.fitchy.store.FeatureContext} that holds all features
	 */
	public static void addFeaturesFromUrl(URL url, FeatureContext context) {
		if(url != null) {
			try {
				File file = new File(url.toURI());
				if(file.exists() && file.canRead()) {
					fillStoreWithFeatures(context, file);
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
	 * into {@link de.kaubisch.fitchy.store.FeatureContext}.
	 * 
	 * @param context an instance of {@link de.kaubisch.fitchy.store.FeatureContext}
	 * @param file {@link File} full of features
	 * @throws FileNotFoundException is thrown if {@link File} is not found
	 */
	private static void fillStoreWithFeatures(FeatureContext context, File file)
			throws FileNotFoundException {
		FitchyOptions option = Fitchy.getOptions();
		FeatureReader reader = null;
		try {
			Constructor<? extends FeatureReader> constructor = option.readerClass.getConstructor(InputStream.class);
			reader = constructor.newInstance(new FileInputStream(file));
			
			Feature feature = null;
			while((feature = reader.read()) != null) {
				context.addFeature(feature);
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

    /**
     * Sets all values of current {@link FitchyOptions} to new values from passed options.
     *
     * @param options {@link FitchyOptions} object with new values
     */
    public static synchronized void setOptions(FitchyOptions options) {
        FitchyOptions oldOptions = Singleton.OPTIONS;
        if(options.readerClass != null) {
            oldOptions.readerClass = options.readerClass;
        }
        oldOptions.enabled = options.enabled;
        oldOptions.disabled = options.disabled;
        oldOptions.statusList = new ArrayList<Enum<? extends FeatureStatus>>(options.statusList);
    }

    /**
     * Create an {@link FeatureObserver} and start to observe the passed object for
     * method calls with {@link de.kaubisch.fitchy.annotation.FeatureSwitch} annotated.
     *
     * @param toObserve object that needs to be observed
     * @param context current {@link de.kaubisch.fitchy.store.FeatureContext}
     * @return returns observed object
     */
    public static <T> T observe(T toObserve, FeatureContext context) {
        FeatureObserver observer = new JavaProxyObserver();
        return observer.observe(toObserve, context);
    }

    public static <T> T observeWithObserver(T toObserve, FeatureContext context, Class<FeatureObserver> observerClass) {
        try {
            FeatureObserver observer = observerClass.newInstance();
            return observer.observe(toObserve, context);
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return toObserve;
    }
}
