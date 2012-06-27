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
import de.kaubisch.fitchy.options.FitchyOptions;

import java.util.HashMap;
import java.util.Map;

public class FeatureContext {

	private Map<String, Feature> featureMap;
	private FitchyOptions options;
	
	
	public FeatureContext(FitchyOptions options) {
		featureMap = new HashMap<String, Feature>();
		this.options = options;
	}
	
	public void addFeature(String key) {
		featureMap.put(key, new Feature(key, options.enabled));
	}
	
	public void addFeature(Feature feature) {
		featureMap.put(feature.name, feature);
	}
	
	public boolean hasFeature(String key) {
		return !"".equals(key) && featureMap.get(key) != null;
	}
	
	public boolean featureHasStatus(String key, FeatureStatus status) {
		if(hasFeature(key)) {
			return featureMap.get(key).status == status;
		}
		
		return false;
	}
	
	public void clear() {
		featureMap.clear();
	}
}
