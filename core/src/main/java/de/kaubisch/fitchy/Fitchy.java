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
	 * Holder class to provide singleton instance of {@link FitchyConfig}
	 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
	 *
	 */
	private static class Singleton {
		public static final FitchyConfig CONFIG = FitchyConfig.getDefault();
	}
	
	/**
	 * private constructor because there is no need for an instance
	 */
	private Fitchy() {
	}
		
	/**
	 * Returns a singleton of {@link FitchyConfig}
	 * 
	 * @return one instance of {@link FitchyConfig}
	 */
	public static FitchyConfig getConfig() {
		return Singleton.CONFIG;
	}

    /**
     * Sets all values of current {@link FitchyConfig} to new values from passed config.
     *
     * @param config {@link FitchyConfig} object with new values
     */
    public static synchronized void setConfig(FitchyConfig config) {
        FitchyConfig oldConfig = Singleton.CONFIG;
        if(config.readerClass != null) {
            oldConfig.readerClass = config.readerClass;
        }
        oldConfig.enabled = config.enabled;
        oldConfig.disabled = config.disabled;
        oldConfig.statusList = new ArrayList<Enum<? extends FeatureStatus>>(config.statusList);
    }

    /**
     * Create an {@link FeatureObserver} and start to observe the passed object for
     * method calls with {@link de.kaubisch.fitchy.annotation.FeatureSwitch} annotated.
     *
     * @param toObserve object that needs to be observed
     * @param context current {@link FeatureContext}
     * @return returns observed object
     */
    public static <T> T observe(T toObserve, FeatureContext context) {
        return observeWithObserver(toObserve, context, getConfig().observerClass);
    }

    public static <T> T observeWithObserver(T toObserve, FeatureContext context, Class<? extends FeatureObserver> observerClass) {
        try {
            FeatureObserver observer = observerClass.newInstance();
            return observer.observe(toObserve, context);
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return toObserve;
    }
}
