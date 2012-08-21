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
package com.googlecode.fitchy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.googlecode.fitchy.exception.StatusNotFoundException;
import com.googlecode.fitchy.exception.UnableToCreateBuilderException;
import com.googlecode.fitchy.internal.JavaProxyObserver;
import com.googlecode.fitchy.internal.PropertyFeatureReader;
import com.googlecode.fitchy.util.Preconditions;



public class Configuration {
	/**
	 * A Builder that builds a {@link Configuration} object. This class is useful
	 * when you want to load configuration from a source or create a new one.
	 * 
	 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
	 */
	public static class Builder {
		
		private Enum<? extends FeatureStatus> featureStatus;
		
		private Class<? extends FeatureObserver> observerClass;
		
		private Class<? extends FeatureReader> readerClass;
		
		private Builder(Enum<? extends FeatureStatus> featureStatus) {
			this.featureStatus = featureStatus;
		}
		
		/**
		 * Creates a {@link Builder} instance that can be used to create instances of
		 * {@link Configuration} class.
		 * This function will set default values for the {@link FeatureObserver} and
		 * {@link FeatureReader} dependencies.
		 * 
		 * @param statusEnum the required enum entry of type {@link FeatureStatus}
		 * @return a new instance of {@link Builder}
		 * 
		 * @throws IllegalArgumentException if statusEnum argument is null
		 */
		public static Builder fromStatus(Enum<? extends FeatureStatus> statusEnum) {
			Preconditions.throwIllegalArgumentExceptionIfNull(statusEnum, "FeatureStatus argument is required to create a Configuration.Builder");
			Builder builder = new Builder(statusEnum);
			builder.readerClass = PropertyFeatureReader.class;
			builder.observerClass = JavaProxyObserver.class;
			return builder;
		}
		
		/**
		 * Creates a {@link Builder} instance that can be used to create instances of 
		 * {@link Configuration} class. That uses an {@link InputStream} to configure
		 * that {@link Configuration} instance.
		 * 
		 * @param is an {@link InputStream} to a fitchy configuration source
		 * @return a new instance of {@link Builder}
		 */
		public static Builder fromStream(InputStream is) {
			Preconditions.throwIllegalArgumentExceptionIfNull(is, "InputStream argument is required to create a Configuration.Builder");
	        Properties fitchyProperties = new Properties();
	        try {
	            fitchyProperties.load(is);
	            Enum<? extends FeatureStatus> status = ((Class<? extends Enum<? extends FeatureStatus>>) Class.forName((String)fitchyProperties.get("fitchy.feature.status"))).getEnumConstants()[0];
	            Builder builder = new Builder(status);
	            builder.readerClass   = (Class<? extends FeatureReader>) Class.forName((String)fitchyProperties.get("fitchy.feature.reader"));
	            builder.observerClass = (Class<? extends FeatureObserver>) Class.forName((String)fitchyProperties.get("fitchy.proxy.observer"));
	            return builder;
	        } catch (IOException e) {
	        	throw new UnableToCreateBuilderException();
	        } catch (ClassNotFoundException e) {
	        	throw new UnableToCreateBuilderException();
	        }
		}

		/**
		 * Sets the {@link FeatureObserver} class to {@link Builder} that is used later to 
		 * create a {@link Configuration} object.
		 * 
		 * @param observerClass an implementing class of {@link FeatureObserver}
		 * @return the current {@link Builder} instance
		 * 
		 * @throws IllegalArgumentException if observerClass argument is null
		 */
		public Builder withObserver(Class<? extends FeatureObserver> observerClass) {
			Preconditions.throwIllegalArgumentExceptionIfNull(observerClass, "FeatureObserver class argument is required to create a Configuration.");
			this.observerClass = observerClass;
			
			return this;
		}

		/**
		 * Sets the {@link FeatureReader} class to {@link Builder} that is used later
		 * to create a {@link Configuration} object.
		 * 
		 * @param readerClass an implementing class of {@link FeatureReader}
		 * @return the current {@link Builder} instance
		 */
		public Builder withReader(Class<? extends FeatureReader> readerClass) {
			Preconditions.throwIllegalArgumentExceptionIfNull(readerClass, "FeatureReader argument is required to create a Configuration.");
			this.readerClass = readerClass;
			return this;
		}

		
		/**
		 * Creates a new instance of {@link Configuration} and set all attributes of this
		 * instance to the values that you configured.
		 * 
		 * @return a new instance of {@link Configuration}
		 * 
		 * @throws IllegalArgumentException if observerClass is null. Please verify that
		 * 									you called {@link Builder#withObserver} first.
		 */
		public Configuration build() {
			Preconditions.throwIllegalArgumentExceptionIfNull(observerClass, "observerClass is required to build Configuration.");
			
			Configuration config = new Configuration();
			config.setObserverClass(observerClass);
			config.setReaderClass(readerClass);
			config.setStatusList(createListFromStatus());
			return config;
		}

		@SuppressWarnings("unchecked")
		private List<Enum<? extends FeatureStatus>> createListFromStatus() {
			return Arrays.asList((Enum<? extends FeatureStatus>[])featureStatus.getClass().getEnumConstants());
		}

		
	}
	
	public List<Enum<? extends FeatureStatus>> statusList;
	
	public FeatureStatus enabledStatus;
	public FeatureStatus disabledStatus;
	
	private Class<? extends FeatureReader> readerClass;

    private Class<? extends FeatureObserver> observerClass;
	
	public Configuration() {
		this.statusList  = new ArrayList<Enum<? extends FeatureStatus>>();
	}

	public static Configuration getDefault() {
        return Builder.fromStream(Configuration.class.getResourceAsStream("/com/googlecode/fitchy/default-fitchy-configuration.properties")).build();
	}

	public FeatureStatus statusOf(String value) {
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

    public FeatureStatus statusOfName(String name) {
        FeatureStatus statusOfName = null;
        for(Enum<? extends FeatureStatus> status : statusList) {
            if(status.name().equals(name)) {
                statusOfName = (FeatureStatus) status;
            }
        }

        return statusOfName;
    }

	public List<Enum<? extends FeatureStatus>> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Enum<? extends FeatureStatus>> statusList) {
		this.statusList = statusList;
		for(Enum<? extends FeatureStatus> status : statusList) {
			FeatureStatus featureStatus = (FeatureStatus)status; 
			if(featureStatus.isEnabledStatus()) {
				setEnabledStatus(featureStatus);
			} else if(featureStatus.isDisabledStatus()) {
				setDisabledStatus(featureStatus);
			}
		}
	}

	public FeatureStatus getEnabledStatus() {
		return enabledStatus;
	}

	protected void setEnabledStatus(FeatureStatus enabledStatus) {
		this.enabledStatus = enabledStatus;
	}

	public FeatureStatus getDisabledStatus() {
		return disabledStatus;
	}

	protected void setDisabledStatus(FeatureStatus disabledStatus) {
		this.disabledStatus = disabledStatus;
	}

	public Class<? extends FeatureReader> getReaderClass() {
		return readerClass;
	}

	protected void setReaderClass(Class<? extends FeatureReader> readerClass) {
		this.readerClass = readerClass;
	}

	public Class<? extends FeatureObserver> getObserverClass() {
		return observerClass;
	}

	public void setObserverClass(Class<? extends FeatureObserver> observerClass) {
		this.observerClass = observerClass;
	}
}
