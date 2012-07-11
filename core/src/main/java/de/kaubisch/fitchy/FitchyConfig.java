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

import de.kaubisch.fitchy.exception.StatusNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class FitchyConfig {
	public List<Enum<? extends FeatureStatus>> statusList;
	
	public FeatureStatus enabled;
	public FeatureStatus disabled;
	
	public Class<? extends FeatureReader> readerClass;

    public Class<? extends FeatureObserver> observerClass;
	
	public FitchyConfig() {
		this.statusList  = new ArrayList<Enum<? extends FeatureStatus>>();
	}

	public static FitchyConfig newOption(Class<? extends Enum<? extends FeatureStatus>> status) {
		FitchyConfig option = new FitchyConfig();
		if(Enum.class.isAssignableFrom(status)) {
			for(Enum<? extends FeatureStatus> e : status.getEnumConstants()) {
				option.statusList.add(e);
                FeatureStatus featureStatus = (FeatureStatus) e;
				if(featureStatus.isEnabledStatus()) {
					option.enabled = featureStatus;
				} else if(featureStatus.isDisabledStatus()) {
					option.disabled= featureStatus;
				}
			}
		}
		return option;
	}

	public static FitchyConfig getDefault() {
        return getFromPropertiesStream(FitchyConfig.class.getResourceAsStream("/de/kaubisch/fitchy/default-fitchy-configuration.properties"));
	}

    public static FitchyConfig getFromPropertiesStream(InputStream is) {
        FitchyConfig options = null;
        Properties fitchyProperties = new Properties();
        try {
            fitchyProperties.load(is);
            options = newOption((Class<? extends Enum<? extends FeatureStatus>>) Class.forName((String)fitchyProperties.get("fitchy.feature.status")));
            options.readerClass = (Class<? extends FeatureReader>) Class.forName((String)fitchyProperties.get("fitchy.feature.reader"));
            options.observerClass = (Class<? extends FeatureObserver>) Class.forName((String)fitchyProperties.get("fitchy.proxy.observer"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return options;
    }


	public FeatureStatus statusOf(String value) throws StatusNotFoundException {
		FeatureStatus statusOfValue = null;
		for(Enum< ? extends FeatureStatus> status : statusList) {
            FeatureStatus featureStatus = (FeatureStatus)status;
			if(featureStatus.getSystemName().equals(value)) {
				statusOfValue = featureStatus;
			}
		}

        if(statusOfValue == null) {
            throw new StatusNotFoundException("status with name '" + value + "' not found.");
        }

		return statusOfValue;
	}

    public FeatureStatus statusOfName(String name) throws StatusNotFoundException {
        FeatureStatus statusOfName = null;
        for(Enum<? extends FeatureStatus> status : statusList) {
            if(status.name().equals(name)) {
                statusOfName = (FeatureStatus) status;
            }
        }

        return statusOfName;
    }
}
