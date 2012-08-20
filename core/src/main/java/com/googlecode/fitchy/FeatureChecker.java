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

/**
 * Abstract class that provides a possibility to check whether
 * a feature is enabledStatus or disabledStatus. When you want to check inline
 * a function and not the function itself then use an anonymous implementation
 * of this abstract class.
 * <br />
 * <strong>example:get different String in case feature is enabledStatus oder disabledStatus.</strong>
 * <br />
 * <pre>
 * {@code
 *  FeatureContext context = new FeatureContext(Fitchy.getConfig());
 *  String resultDependingOnFeature = new FeatureChecker<String>(context, "test.feature") {
 *      public String onFeatureDisabled() {
 *          return "feature is disabledStatus";
 *      }
 *
 *      public String onFeatureEnabled() {
 *          return "feature is enabledStatus";
 *      }
 *
 *  }).run();
 *  System.out,println(resultDependingOnFeature); //should print "feature is disabledStatus"
 * }
 * </pre>
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/11/12
 * Time: 6:55 PM
 *
 * @param <T> class that this checker should return when calling {@link com.googlecode.fitchy.FeatureChecker#run()}
 */
public abstract class FeatureChecker<T> {

	private FeatureContext context;
	private String featureName;


	public FeatureChecker(FeatureContext storage, String featureName) {
		this.context = storage;
		this.featureName = featureName;
	}

    /**
     * This function is called when the feature is enabledStatus.
     *
     * @return T result that should be returned when feature is enabledStatus.
     */
	public abstract T onFeatureEnabled();

    /**
     * This function is called when the feature is disabledStatus.
     *
     * @return T result that should be returned when feature is disabledStatus.
     */
	public abstract T onFeatureDisabled();

    /**
     * Executing this function starts calling {@link FeatureContext} and
     * ask if feature is enabledStatus. Then it calls either {@link FeatureChecker#onFeatureEnabled()}
     * or {@link FeatureChecker#onFeatureDisabled()} of the implementation and
     * return the results.
     *
     * @return T result that is returned from implementation.
     */
	public T run() {
		T result = null;
		if(context.hasFeature(featureName)) {
			result = onFeatureEnabled();
		} else {
			result = onFeatureDisabled();
		}
		
		return result;
	}
}
