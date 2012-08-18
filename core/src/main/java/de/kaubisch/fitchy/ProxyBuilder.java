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

import de.kaubisch.fitchy.exception.CannotCreateProxyException;
import de.kaubisch.fitchy.util.Preconditions;

public class ProxyBuilder {

	private FeatureContext context;
	
	private FeatureObserver observer;
	
	private ProxyBuilder(FeatureContext context, FeatureObserver observer) {
		this.context = context;
		this.observer = observer;
	}
	
	/**
	 * Create a {@link ProxyBuilder} to proxying target objects.
	 * This function throws an {@link IllegalArgumentException} if the context argument is null.
	 * 
	 * @param context current {@link FeatureContext}
	 * @return a new {@link ProxyBuilder} instance
	 * 
	 * @throws IllegalArgumentException 	is thrown if context is null
	 * @throws CannotCreateProxyException 	is thrown if function cannot create a FeatureObserver
	 * 										from given observerClass attribute of context {@link Configuration}
	 */
    public static ProxyBuilder fromContext(FeatureContext context) {
    	Preconditions.throwIllegalArgumentExceptionIfNull(context, "FeatureContext argument is required to create ProxyBuilder.");
    	Preconditions.throwIllegalArgumentExceptionIfNull(context.getConfig().getObserverClass(), "given Configuration object of context doesn't have an oberservClass defined.");
    	try {
			FeatureObserver observer = context.getConfig().getObserverClass().newInstance();
			return new ProxyBuilder(context, observer);
		} catch (InstantiationException e) {
			throw new CannotCreateProxyException("unable to create FeatureObserver instance", e);
		} catch (IllegalAccessException e) {
			throw new CannotCreateProxyException("unable to create FeatureObserver instance", e);
		}
    }
    
	/**
     * Create an {@link FeatureObserver} and start to observe the passed object for
     * method calls with {@link de.kaubisch.fitchy.annotation.FeatureSwitch} annotated.
     *
     * @param toObserve object that needs to be observed
     * @param context current {@link FeatureContext}
     * @return returns observed object
     */
	public <T> T build(T origin) {
		return observer.observe(origin, context);
	}

	/**
	 * Sets a new {@link FeatureObserver} to current {@link ProxyBuilder}.
	 * This function throws an {@link IllegalArgumentException} if the observer argument
	 * is null.
	 * This function is used when you want to proxy the target object with your own
	 * {@link FeatureObserver}. Otherwise this builder uses the observer class that is
	 * defined in context {@link Configuration}.
	 * 
	 * @param observer new {@link FeatureObserver}
	 * @return current {@link ProxyBuilder} instance
	 * 
	 * @throws IllegalArgumentException is thrown if {@link FeatureObserver} is null
	 */
	public ProxyBuilder withObserver(FeatureObserver observer) {
		Preconditions.throwIllegalArgumentExceptionIfNull(observer, "FeatureObserver argument is required.");
		this.observer = observer;
		return this;
	}
}
