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

import de.kaubisch.fitchy.exception.FeatureAlreadyExistsException;
import de.kaubisch.fitchy.util.Preconditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link FeatureContext} contains all loaded {@link Feature} items of
 * Fitchy and provide methods to query these items.
 *
 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
 */
public class FeatureContext {
	/**
	 * {@link Builder} takes an {@link URL} or an {@link InputStream} to build
	 * a {@link FeatureContext}. There is an optional function with them you can set
	 * a custom {@link Configuration}.
	 * 
	 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
	 */
	public static final class Builder {

		private Configuration configuration;
		
		private URL url;
		private InputStream is;

		
		private Builder(Configuration configuration) {
			this.configuration = configuration;
		}
		
		private Builder() {
			this(Configuration.getDefault());
		}
		
		/**
		 * Setup a {@link Builder} with an URL that point to a feature resource
		 * file. It sets the default {@link Configuration};
		 * 
		 * @param url an {@link URL} to a resource with feature configuration
		 * 
		 * @return a new {@link Builder} that is configured to read from an {@link URL}
		 * @throws IllegalArgumentException if given url is null
		 */
		public static Builder fromUrl(URL url) {
			Preconditions.throwIllegalArgumentExceptionIfNull(url, "URL argument is required to create ContextBuilder");
			Builder builder = create();
			builder.url = url;
			
			return builder;
		}
		
		public static Builder fromStream(InputStream is) {
			Preconditions.throwIllegalArgumentExceptionIfNull(is, "InputStream argument is required to create ContextBuilder");
			Builder builder = create();
			builder.is = is;
			
			return builder;
		}

		
		/**
		 * Setup a {@link Builder} without an {@link URL} or an {@link InputStream}.
		 * It sets the {@link Configuration} to default. This function is used when you want
		 * to create an empty {@link FeatureContext}.
		 * 
		 * @return a new {@link Builder} without an {@link URL} and {@link InputStream}
		 */
		public static Builder create() {
			return new Builder();
		}

		/**
		 * Setup an alternative {@link Configuration} for current {@link Builder} instance. This {@link Configuration}
		 * is used to instanciate the {@link FeatureContext} later.
		 *  
		 * @param config alternative {@link Configuration}
		 * @return current {@link Builder} instance
		 */
		public Builder withConfig(Configuration config) {
			Preconditions.throwIllegalArgumentExceptionIfNull(config, "Configuration is required");
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
			return new FeatureContext(configuration);
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
			Configuration option = context.getConfig();
			FeatureReader reader = null;
			try {
				Constructor<? extends FeatureReader> constructor = option.readerClass.getConstructor(InputStream.class, Configuration.class);
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
	
	private Map<String, Feature> featureMap;
	private Configuration config;

    /**
     * Initializes a new {@link FeatureContext} with given {@link Configuration}
     *
     * @param config current {@link Configuration} instance
     */
	public FeatureContext(Configuration config) {
		featureMap = new HashMap<String, Feature>();
		this.config = config;
	}

    /**
     * Adds a new {@link Feature} to context. If the Feature is already add to context a
     * {@link FeatureAlreadyExistsException} is thrown. The passed {@link String} attribute 'key'
     * represents the name of the new feature.
     * If the required attribute 'key' is null or empty an IllegalArgumentException is thrown.
     * When function added Feature to this context it returns this newly added object so you can
     * work with it. This instance has the {@link FeatureStatus} enabled.
     *
     * @param key name of {@link Feature}
     * @return a new {@link Feature} implementation that was added to context
     * @throws FeatureAlreadyExistsException is thrown if the feature already exists in context
     */
	public Feature addFeature(String key) {
        if(key == null || "".equals(key.trim())) {
            throw new IllegalArgumentException("key argument must be a value");
        }
        if(hasFeature(key)) {
            throw new FeatureAlreadyExistsException("feature with name " + key +" already exists.");
        }

		Feature feature = new Feature(key, config.enabled);
        featureMap.put(key, feature);
        return feature;
	}

    /**
     * This function has the same functionality like {@link FeatureContext#addFeature(String)}
     * but instead set the {@link FeatureStatus} of the new feature is enabled it
     * takes the status of the passed object.
     * If the required attribute 'feature' is null the function throws an {@link IllegalArgumentException}.
     *
     * @param feature new {@link Feature} that needs to be add to context
     * @return if feature is added to context the feature argument is returned.
     * @throws FeatureAlreadyExistsException is thrown when feature already exists in context
     */
	public Feature addFeature(Feature feature) {
        if(feature == null) {
            throw new IllegalArgumentException("feature argument must be an instance");
        }
        if(hasFeature(feature.getName())) {
            throw new FeatureAlreadyExistsException("feature with name " + feature.getName() + " already exists.");
        }

		featureMap.put(feature.getName(), feature);

        return feature;
	}

    /**
     * Checks whether a {@link Feature} with a name equals the key argument is found in context.
     * If it exists in context it returns true otherwise it returns false.
     * It also returns false when the key argument is null or empty.
     *
     * @param key desired feature name
     * @return returns boolean whether feature with name was found in context
     */
	public boolean hasFeature(String key) {
		return key != null && !"".equals(key) && featureMap.get(key) != null;
	}

    /**
     * Retrieves a {@link Feature} from context and checks if it has the desired {@link FeatureStatus}
     * or not.
     * If the required key argument is null or empty the function throws an {@link IllegalArgumentException}.
     * Should the function didn't find a {@link Feature} with this name it will handle this situation
     * that this Feature doesn't have the right status.
     *
     * @param key name of the feature that needs to be checked against status
     * @param status a desired {@link FeatureStatus} that the found {@link Feature} must have.
     * @return returns boolean whether this feature has the status or not.
     */
	public boolean featureHasStatus(String key, FeatureStatus status) {
		if(key == null || "".equals(key)) {
            throw new IllegalArgumentException("key argument is required");
        }

        if(hasFeature(key)) {
			return featureMap.get(key).getStatus() == status;
		}
		
		return false;
	}

    /**
     * Clears the context.
     */
	public void clear() {
		featureMap.clear();
	}

    /**
     * Returns the size of this context. That size says how many {@link Feature} objects
     * were added to this context.
     *
     * @return amount of added {@link Feature} objects
     */
    public int size() {
        return featureMap.size();
    }

    /**
     * Returns the config the context is constructed from.
     *
     * @return current {@link Configuration} from context
     */
    public Configuration getConfig() {
        return config;
    }
}
