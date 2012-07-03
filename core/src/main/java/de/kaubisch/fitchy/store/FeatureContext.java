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
	
	
	public FeatureContext(FitchyOptions options) {
		featureMap = new HashMap<String, Feature>();
		this.options = options;
	}
	
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
	
	public boolean hasFeature(String key) {
		return !"".equals(key) && featureMap.get(key) != null;
	}
	
	public boolean featureHasStatus(String key, FeatureStatus status) {
		if(key == null) {
            throw new IllegalArgumentException("key argument is required");
        }

        if(hasFeature(key)) {
			return featureMap.get(key).status == status;
		}
		
		return false;
	}
	
	public void clear() {
		featureMap.clear();
	}

    public int size() {
        return featureMap.size();
    }
}
