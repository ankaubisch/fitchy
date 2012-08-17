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

import de.kaubisch.fitchy.internal.Preconditions;

public class ContextBuilder {

	private FitchyConfig configuration;
	
	private URL url;
	private InputStream is;

	
	private ContextBuilder(FitchyConfig configuration) {
		this.configuration = configuration;
	}
	
	private ContextBuilder() {
		this(FitchyConfig.getDefault());
	}
	
	/**
	 * Setup a {@link ContextBuilder} with an URL that point to a feature resource
	 * file. It sets the default {@link FitchyConfig};
	 * 
	 * @param url an {@link URL} to a resource with feature configuration
	 * 
	 * @return a new {@link ContextBuilder} that is configured to read from an {@link URL}
	 * @throws IllegalArgumentException if given url is null
	 */
	public static ContextBuilder fromUrl(URL url) {
		Preconditions.throwIllegalArgumentExceptionIfNull(url, "URL argument is required to create ContextBuilder");
		ContextBuilder builder = create();
		builder.url = url;
		
		return builder;
	}
	
	public static ContextBuilder fromStream(InputStream is) {
		Preconditions.throwIllegalArgumentExceptionIfNull(is, "InputStream argument is required to create ContextBuilder");
		ContextBuilder builder = create();
		builder.is = is;
		
		return builder;
	}

	
	/**
	 * Setup a {@link ContextBuilder} without an {@link URL} or an {@link InputStream}.
	 * It sets the {@link FitchyConfig} to default. This function is used when you want
	 * to create an empty {@link FeatureContext}.
	 * 
	 * @return a new {@link ContextBuilder} without an {@link URL} and {@link InputStream}
	 */
	public static ContextBuilder create() {
		ContextBuilder builder = new ContextBuilder();
		return builder;
	}

	/**
	 * Setup an alternative {@link FitchyConfig} for current {@link ContextBuilder} instance. This {@link FitchyConfig}
	 * is used to instanciate the {@link FeatureContext} later.
	 *  
	 * @param config alternative {@link FitchyConfig}
	 * @return current {@link ContextBuilder} instance
	 */
	public ContextBuilder withConfig(FitchyConfig config) {
		Preconditions.throwIllegalArgumentExceptionIfNull(config, "FitchyConfig is required");
		this.configuration = config;
		
		return this;
	}
	
	public FeatureContext build() {
		FeatureContext context = null;
		if(url != null) {
			context = createFromUrl(url);
		} else if (is != null) {
			context = createFromStream(is);
		} else {
			context = createEmpty();
		}
		
		return context;
	}


	
	/**
	 * Loads a resource with feature configuration from an {@link URL} and stores all
	 * of this in a new {@link FeatureContext} and returns it.
	 *  
	 * @param url an {@link URL} to a resource with feature configuration
	 * 
	 * @return a new {@link FeatureContext} with loaded {@link Feature} items
	 * @throws IllegalArgumentException if given url is null
	 */
	private FeatureContext createFromUrl(URL urlToFeatures) {
		Preconditions.throwIllegalArgumentExceptionIfNull(urlToFeatures, "URL argument is required to create context");
		FeatureContext newContext = createEmpty();
		addFeaturesFromUrl(urlToFeatures, newContext);
		
		return newContext;
	}
	
	/**
	 * Loads a resource with feature configuration from an {@link InputStream} and stores all
	 * of this in a new {@link FeatureContext} and returns it.
	 * 
	 * @param is an {@link InputStream} from a resource with feature configurations
	 * @return a new {@link FeatureContext} with loaded {@link Feature} items
	 * @throws an {@link IllegalArgumentException} if given InputStream is null
	 */
	private FeatureContext createFromStream(InputStream is) {
		Preconditions.throwIllegalArgumentExceptionIfNull(is, "InputStream argument is required to create context");
		FeatureContext newContext = createEmpty();
		fillStoreWithFeatures(newContext, is);
		
		return newContext;
	}
	
	/**
	 * Creates an empty new instance of {@link FeatureContext).
	 * 
	 * @return a new empty instance of {@link FeatureContext}
	 */
	public FeatureContext createEmpty() {
		FeatureContext newContext = new FeatureContext(configuration);

		return newContext;
	}

	/**
	 * Add all features from url resource to existing {@link FeatureContext}.
	 * This overrides existing {@link Feature} added to storage.
	 * 
	 * @param url an {@link URL} to a resource that contains features
	 * @param context a {@link FeatureContext} that holds all features
	 */
	private void addFeaturesFromUrl(URL url, FeatureContext context) {
		if(url != null) {
			try {
				File file = new File(url.toURI());
				if(file.exists() && file.canRead()) {
					fillStoreWithFeatures(context, new FileInputStream(file));
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
	 * into {@link FeatureContext}.
	 * 
	 * @param context an instance of {@link FeatureContext}
	 * @param is {@link InputStream} of a container with features
	 * @throws FileNotFoundException is thrown if {@link File} is not found
	 */
	private void fillStoreWithFeatures(FeatureContext context, InputStream is) {
		FitchyConfig option = Fitchy.getConfig();
		FeatureReader reader = null;
		try {
			Constructor<? extends FeatureReader> constructor = option.readerClass.getConstructor(InputStream.class, FitchyConfig.class);
			reader = constructor.newInstance(is, option);
			
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

}
