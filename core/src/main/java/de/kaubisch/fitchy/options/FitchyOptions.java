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
package de.kaubisch.fitchy.options;

import java.util.ArrayList;
import java.util.List;

import de.kaubisch.fitchy.FeatureStatus;
import de.kaubisch.fitchy.loader.FeatureReader;
import de.kaubisch.fitchy.loader.PropertyFeatureReader;


public class FitchyOptions {
	public List<FeatureStatus> statusList;
	
	public FeatureStatus enabled;
	public FeatureStatus disabled;
	
	public Class<? extends FeatureReader> readerClass;
	
	private FitchyOptions() {
		this.statusList  = new ArrayList<FeatureStatus>();
	}
	
	public static FitchyOptions newOption(Class<? extends FeatureStatus> status) {
		FitchyOptions option = new FitchyOptions();
		if(Enum.class.isAssignableFrom(status)) {
			for(FeatureStatus e : status.getEnumConstants()) {
				option.statusList.add(e);
				if(e.isEnabledStatus()) {
					option.enabled = e;
				} else if(e.isDisabledStatus()) {
					option.disabled= e;
				}
			}
		}
		return option;
	}

	public static FitchyOptions getDefault() {
		FitchyOptions options = newOption(DefaultFeatureStatus.class);
		options.readerClass = PropertyFeatureReader.class;
		return options;
	}
	
	public FeatureStatus statusOf(String value) {
		FeatureStatus statusOfValue = disabled;
		for(FeatureStatus status : statusList) {
			if(status.getSystemName().equals(value)) {
				statusOfValue = status;
			}
		}
		
		return statusOfValue;
	}
}
