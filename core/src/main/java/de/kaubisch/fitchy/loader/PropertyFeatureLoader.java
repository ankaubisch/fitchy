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
package de.kaubisch.fitchy.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.kaubisch.fitchy.Feature;
import de.kaubisch.fitchy.FeatureStatus;

public class PropertyFeatureLoader extends FeatureLoader {

	private InputStream is;
	
	public PropertyFeatureLoader(InputStream is) {
		super();
		this.is = is;
	}
	
	@Override
	protected List<Feature> getFeatureList() {
		Properties properties = new Properties();
		List<Feature> featureList = new ArrayList<Feature>();
		try {
			properties.load(is);
			for(Object key : properties.keySet()) {
				String keyName = String.valueOf(key);
				String enabledString = properties.getProperty(keyName, option.disabled.getSystemName());
				FeatureStatus status = option.statusOf(enabledString);
				if(status != null) {
					Feature feature = new Feature(keyName, status);
					featureList.add(feature);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return featureList;
	}
}
