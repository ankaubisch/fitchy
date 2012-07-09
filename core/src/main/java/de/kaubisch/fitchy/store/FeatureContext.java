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
package de.kaubisch.fitchy.store;

import de.kaubisch.fitchy.Feature;
import de.kaubisch.fitchy.FeatureStatus;
import de.kaubisch.fitchy.exception.FeatureAlreadyExistsException;
import de.kaubisch.fitchy.options.FitchyOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link FeatureContext} contains all loaded {@link Feature} items of
 * Fitchy and provide methods to query these items.
 *
 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
 */
public class FeatureContext {

	private Map<String, Feature> featureMap;
	private FitchyOptions options;

    /**
     * Initializes a new {@link FeatureContext} with given {@link FitchyOptions}
     *
     * @param options current {@link FitchyOptions} instance
     */
	public FeatureContext(FitchyOptions options) {
		featureMap = new HashMap<String, Feature>();
		this.options = options;
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
	public Feature addFeature(String key) throws FeatureAlreadyExistsException {
        if(key == null || "".equals(key.trim())) {
            throw new IllegalArgumentException("key argument must be a value");
        }
        if(hasFeature(key)) {
            throw new FeatureAlreadyExistsException("feature with name " + key +" already exists.");
        }

		Feature feature = new Feature(key, options.enabled);
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
	public Feature addFeature(Feature feature) throws FeatureAlreadyExistsException {
        if(feature == null) {
            throw new IllegalArgumentException("feature argument must be an instance");
        }
        if(hasFeature(feature.name)) {
            throw new FeatureAlreadyExistsException("feature with name " + feature.name + " already exists.");
        }

		featureMap.put(feature.name, feature);

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
			return featureMap.get(key).status == status;
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
}
